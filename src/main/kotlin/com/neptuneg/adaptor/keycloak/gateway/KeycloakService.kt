package com.neptuneg.adaptor.keycloak.gateway

import com.neptuneg.domain.entity.User
import com.neptuneg.domain.entity.UserInfo
import com.neptuneg.domain.entity.serializer.Serializer
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.rmi.UnexpectedException
import javax.ws.rs.core.Response
import com.neptuneg.config.KeycloakConfig

class KeycloakService(
    private val config: KeycloakConfig
) {
    private val adminKeycloak = buildKeycloak(config.adminUsername, config.adminPassword)
    private val adminKeycloakRealm = adminKeycloak.realm(config.realm)
    private val adminKeycloakRealmUsers = adminKeycloakRealm.users()
    private val userInfoEndpointUrl = config.userinfoEndpoint.toHttpUrl()

    data class UserAttributes(
        val email: String? = null,
        val password: String? = null,
        val username: String? = null,
        val bio: String? = null,
        val image: String? = null
    )

    companion object {
        val client: OkHttpClient by lazy {
            OkHttpClient.Builder().build()
        }

        fun authorizationGet(url: HttpUrl, bearerToken: String): okhttp3.Response {
            val request = Request.Builder().url(url).get()
                .addHeader("Authorization", "Bearer $bearerToken")
                .build()
            return client.newCall(request).execute()
        }
    }

    fun createUser(user: User, password: String): Result<User> {
        val userRepresentation = UserRepresentation().apply {
            email = user.email
            isEmailVerified = true
            isEnabled = true
            username = user.username
            credentials = listOf(
                CredentialRepresentation().apply {
                    value = password
                    type = CredentialRepresentation.PASSWORD
                    isTemporary = false
                }
            )
        }
        val response = adminKeycloakRealmUsers.create(userRepresentation)

        return if (response.statusInfo == Response.Status.CREATED) {
            Result.success(user)
        } else {
            Result.failure(Exception(response.statusInfo.reasonPhrase))
        }
    }

    fun getUserInfo(token: String): Result<UserInfo> {
        return runCatching {
            authorizationGet(userInfoEndpointUrl, token).let { response ->
                if (!response.isSuccessful) throw UnexpectedException("Unexpected code ${response.message}")

                response.body?.let { Serializer.moshi.adapter(UserInfo::class.java).fromJson(it.source()) }
            }!!
        }
    }

    fun updateUser(userId: String, userAttributes: UserAttributes): Result<Unit> {
        return runCatching {
            val userRepresentation = userAttributes.toUserRepresentation()
            adminKeycloakRealmUsers.get(userId).update(userRepresentation)
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

    private fun UserAttributes.toUserRepresentation(): UserRepresentation {
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
