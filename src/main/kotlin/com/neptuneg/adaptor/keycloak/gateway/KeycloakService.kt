package com.neptuneg.adaptor.keycloak.gateway

import com.neptuneg.infrastructure.config.KeycloakConfig
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.util.UUID
import javax.ws.rs.core.Response

@Suppress("TooManyFunctions")
class KeycloakService(
    private val config: KeycloakConfig
) {
    private val adminKeycloak = buildKeycloak(config.adminUsername, config.adminPassword)
    private val adminKeycloakRealm = adminKeycloak.realm(config.realm)
    private val adminKeycloakRealmUsers = adminKeycloakRealm.users()

    private val usersById: MutableMap<UUID, KeyCloakUserProfile> = mutableMapOf()
    private val usersByUsername: MutableMap<String, KeyCloakUserProfile> = mutableMapOf()

    data class KeyCloakUserProfile(
        val id: UUID,
        val username: String,
        val email: String,
        val bio: String,
        val image: String,
    )

    fun createUser(username: String, email: String, password: String): Result<KeyCloakUserProfile> {
        val userRepresentation = buildUserRepresentation(username, email, password)
        val response = adminKeycloakRealmUsers.create(userRepresentation)

        return if (response.statusInfo == Response.Status.CREATED) {
            findUserByUsername(username)
        } else {
            Result.failure(Exception(response.statusInfo.reasonPhrase))
        }
    }

    fun findUserByToken(token: String): Result<KeyCloakUserProfile> {
        return OAuthClient.getUserInfo(config.userinfoEndpoint, token).mapCatching { it.toUserProfile() }
    }

    fun findUserById(id: UUID): Result<KeyCloakUserProfile> {
        return runCatching {
            usersById[id] ?: run {
                adminKeycloakRealmUsers.get(id.toString()).toRepresentation().toUserProfile().apply {
                    usersById[id] = this
                }
            }
        }
    }

    fun findUserByUsername(username: String): Result<KeyCloakUserProfile> {
        return runCatching {
            usersByUsername[username] ?: run {
                adminKeycloakRealmUsers.searchByUsername(username, true).map { it.toUserProfile() }.first().apply {
                    usersByUsername[username] = this
                }
            }
        }
    }

    fun updateUser(profile: KeyCloakUserProfile): Result<Unit> {
        return runCatching {
            val user = adminKeycloakRealmUsers.get(profile.id.toString())
            val userRepresentation = user.toRepresentation().apply {
                username = profile.username
                email = profile.email
                singleAttribute("bio", profile.bio)
                singleAttribute("image", profile.image)
            }
            user.update(userRepresentation).apply {
                usersById[profile.id] = userRepresentation.toUserProfile()
            }
        }
    }

    fun updateUserPassword(userId: UUID, password: String): Result<Unit> {
        return runCatching {
            val user = adminKeycloakRealmUsers.get(userId.toString())
            val userRepresentation = user.toRepresentation().apply {
                credentials = listOf(
                    CredentialRepresentation().apply {
                        value = password
                        type = CredentialRepresentation.PASSWORD
                        isTemporary = false
                    }
                )
            }
            user.update(userRepresentation)
        }
    }

    fun authenticate(email: String, password: String): Result<String> {
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

    private fun OAuthClient.OAuthUserInfo.toUserProfile() = KeyCloakUserProfile(
        id = sub,
        username = preferredUsername,
        email = email,
        bio = bio ?: "",
        image = image ?: "",
    )

    private fun UserRepresentation.toUserProfile() = KeyCloakUserProfile(
        id = UUID.fromString(id),
        username = username,
        email = email,
        bio = firstAttribute("bio") ?: "",
        image = firstAttribute("image") ?: "",
    )
}
