package com.neptuneg.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

data class Keycloak(
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
    val authorizeUrl: String = "$host/realms/$realm/protocol/openid-connect/auth"
    val accessTokenUrl: String = "$host/realms/$realm/protocol/openid-connect/token"
}

data class Server(
    val port: Int,
    val keycloak: Keycloak,
)

data class Database(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val driverClassName: String,
)

data class Config(
    val server: Server,
    val database: Database
) {
    companion object {
        fun buildFromYamlResource(resource: String): Config {
            return ConfigLoaderBuilder.default().addResourceSource(resource).build().loadConfigOrThrow()
        }
    }
}
