package com.neptuneg.adaptor.keycloak.gateway

import com.neptuneg.domain.entity.User
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import javax.ws.rs.core.Response
import com.neptuneg.config.Keycloak as KeycloakConfig

class KeycloakService(
    private val config: KeycloakConfig
) {
    private val adminKeycloak = buildKeycloak(config.adminUsername, config.adminPassword)
    private val adminKeycloakRealm = adminKeycloak.realm(config.realm)
    private val adminKeycloakRealmUsers = adminKeycloakRealm.users()

    fun createUser(user: User): Result<Unit> {
        val userRepresentation = user.toUserRepresentation()
        val response = adminKeycloakRealmUsers.create(userRepresentation)

        return if (response.statusInfo == Response.Status.CREATED) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.statusInfo.reasonPhrase))
        }
    }

    fun updateUser(user: User, userId: String): Result<Unit> {
        val ur =user.toUserRepresentation()
        adminKeycloakRealmUsers.get(userId).update(ur)
        return Result.success(Unit)
    }

    fun requestToken(user: User): Result<String> {
        return runCatching {
            buildKeycloak(user.email!!, user.password!!).tokenManager().accessToken.token
        }
    }

    private fun buildKeycloak(username: String, password: String): Keycloak = KeycloakBuilder.builder()
        .serverUrl(config.host).grantType(OAuth2Constants.PASSWORD).realm(config.realm)
        .clientId(config.clientId).clientSecret(config.clientSecret)
        .username(username).password(password).scope("openid")
        .build()

    private fun User.toUserRepresentation(): UserRepresentation {
        val user = this
        return UserRepresentation().apply {
            email = user.email
            isEmailVerified = true
            isEnabled = true
            username = user.username
            credentials = listOf(
                CredentialRepresentation().apply {
                    value = user.password
                    type = CredentialRepresentation.PASSWORD
                    isTemporary = false
                }
            )
            attributes = mapOf(
                "bio" to listOf(user.bio),
                "image" to listOf(user.image),
            )
        }
    }
}
