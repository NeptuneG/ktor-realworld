package com.neptuneg.infrastructure.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

data class KeycloakConfig(
    val host: String,
    val realm: String,
    val clientId: String,
    val clientSecret: String,
    val adminUsername: String,
    val adminPassword: String,
    val redirectUri: String,
) {
    val jwtIssuer: String = "$host/realms/$realm"
    val jwksUri: String = "$host/realms/$realm/protocol/openid-connect/certs"
    val authorizationEndpoint: String = "$host/realms/$realm/protocol/openid-connect/auth"
    val tokenEndpoint: String = "$host/realms/$realm/protocol/openid-connect/token"
    val userinfoEndpoint: String = "$host/realms/$realm/protocol/openid-connect/userinfo"
}

data class ServerConfig(
    val port: Int,
    val keycloak: KeycloakConfig,
)

data class DatabaseConfig(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val driverClassName: String,
)

data class Config(
    val server: ServerConfig,
    val database: DatabaseConfig
) {
    companion object {
        fun buildFromYamlResource(resource: String): Config {
            return ConfigLoaderBuilder.default().addResourceSource(resource).build().loadConfigOrThrow()
        }
    }
}
