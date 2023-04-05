package com.neptuneg.adaptor.keycloak.gateway

import com.neptuneg.domain.entities.User
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import javax.ws.rs.core.Response
import com.neptuneg.infrastructure.config.KeycloakConfig
import java.util.UUID

class KeycloakService(
    private val config: KeycloakConfig
) {
    private val adminKeycloak = buildKeycloak(config.adminUsername, config.adminPassword)
    private val adminKeycloakRealm = adminKeycloak.realm(config.realm)
    private val adminKeycloakRealmUsers = adminKeycloakRealm.users()

    private val usersById: MutableMap<UUID, User> = mutableMapOf()
    private val usersByUsername: MutableMap<String, User> = mutableMapOf()

    fun createUser(username: String, email: String, password: String): Result<User> {
        val userRepresentation = buildUserRepresentation(username, email, password)
        val response = adminKeycloakRealmUsers.create(userRepresentation)

        return if (response.statusInfo == Response.Status.CREATED) {
            findUserByUsername(username)
        } else {
            Result.failure(Exception(response.statusInfo.reasonPhrase))
        }
    }

    fun findUserByToken(token: String): Result<User> {
        return OAuthClient.getUserInfo(config.userinfoEndpoint, token).mapCatching { it.toUser() }
    }

    fun findUser(id: UUID): Result<User> {
        return runCatching {
            usersById[id] ?: run {
                adminKeycloakRealmUsers.get(id.toString()).toRepresentation().toUser().apply {
                    usersById[id] = this
                }
            }
        }
    }

    fun findUserByUsername(username: String): Result<User> {
        return runCatching {
            usersByUsername[username] ?: run {
                adminKeycloakRealmUsers.searchByUsername(username, true).map { it.toUser() }.first().apply {
                    usersByUsername[username] = this
                }
            }
        }
    }

    fun updateUser(userId: UUID, userAttributes: Map<String, String?>): Result<Unit> {
        return runCatching {
            val user = adminKeycloakRealmUsers.get(userId.toString())
            val userRepresentation = user.toRepresentation().apply {
                userAttributes["username"]?.let { this.username = it }
                userAttributes["email"]?.let { this.email = it }
                userAttributes["password"]?.let { password ->
                    this.credentials = listOf(
                        CredentialRepresentation().apply {
                            value = password
                            type = CredentialRepresentation.PASSWORD
                            isTemporary = false
                        }
                    )
                }
                userAttributes["bio"]?.let { this.singleAttribute("bio", it) }
                userAttributes["image"]?.let { this.singleAttribute("image", it) }
            }
            user.update(userRepresentation).apply {
                usersById[userId] = userRepresentation.toUser()
            }
        }
    }

    fun requestToken(email: String, password: String): Result<String> {
        return runCatching {
            buildKeycloak(email, password).tokenManager().accessToken.token
        }
    }

    private fun buildKeycloak(email: String, password: String): Keycloak = KeycloakBuilder.builder()
        .serverUrl(config.host).grantType(OAuth2Constants.PASSWORD).realm(config.realm)
        .clientId(config.clientId).clientSecret(config.clientSecret)
        .username(email).password(password).scope("openid")
        .build()

    private fun buildUserRepresentation(
        username: String,
        email: String,
        password: String
    ) = UserRepresentation().apply {
        this.email = email
        isEmailVerified = true
        isEnabled = true
        this.username = username
        credentials = listOf(
            CredentialRepresentation().apply {
                value = password
                type = CredentialRepresentation.PASSWORD
                isTemporary = false
            }
        )
    }

    private fun OAuthClient.OAuthUserInfo.toUser() = User(
        id = sub,
        username = preferredUsername,
        email = email,
        bio = bio ?: "",
        image = image ?: "",
    )

    private fun UserRepresentation.toUser() = User(
        id = UUID.fromString(id),
        username = username,
        email = email,
        bio = firstAttribute("bio") ?: "",
        image = firstAttribute("image") ?: "",
    )
}
