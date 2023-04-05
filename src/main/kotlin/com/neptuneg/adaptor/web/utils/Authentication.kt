package com.neptuneg.adaptor.web.utils

import com.auth0.jwk.UrlJwkProvider
import com.neptuneg.infrastructure.config.KeycloakConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.oauth
import io.ktor.server.response.respond
import java.net.URI

fun Application.installAuthentication(keycloakConfig: KeycloakConfig) {
    install(Authentication) {
        setupJWT(keycloakConfig)
    }
}

fun AuthenticationConfig.setupOAuth(keycloakConfig: KeycloakConfig) {
    val keycloakOAuthProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = "keycloak",
        authorizeUrl = keycloakConfig.authorizationEndpoint,
        accessTokenUrl = keycloakConfig.tokenEndpoint,
        clientId = keycloakConfig.clientId,
        clientSecret = keycloakConfig.clientSecret,
        requestMethod = HttpMethod.Post,
    )

    oauth("keycloakOAuth") {
        client = HttpClient(Apache)
        providerLookup = { keycloakOAuthProvider }
        urlProvider = { keycloakConfig.redirectUri }
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
            if (credential.payload.getClaim("email").asString() != "") {
                JWTPrincipal(credential.payload)
            } else {
                null
            }
        }
        challenge { _, _ -> call.respond(HttpStatusCode.Unauthorized) }
    }
}
