package com.neptuneg.adaptor.web.utils

import com.auth0.jwk.UrlJwkProvider
import com.neptuneg.infrastructure.config.KeycloakConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import java.net.URI

fun Application.installAuthentication(keycloakConfig: KeycloakConfig) {
    install(Authentication) {
        setupJWT(keycloakConfig)
    }
}

@Suppress("MagicNumber")
fun AuthenticationConfig.setupJWT(keycloakConfig: KeycloakConfig) {
    val provider = UrlJwkProvider(URI(keycloakConfig.jwksUri).normalize().toURL())

    jwt("keycloakJWT") {
        realm = keycloakConfig.realm

        authSchemes("Token")
        verifier(provider, keycloakConfig.jwtIssuer) { acceptLeeway(3) }
        validate { credential ->
            if (credential.payload.getClaim("email").asString().isNotEmpty()) {
                JWTPrincipal(credential.payload)
            } else {
                null
            }
        }
        challenge { _, _ -> call.respond(HttpStatusCode.Unauthorized) }
    }
}
