package com.neptuneg.adaptor.web

import com.auth0.jwk.UrlJwkProvider
import com.neptuneg.adaptor.web.controller.sample
import com.neptuneg.adaptor.web.controller.setupMonitoringRouting
import com.neptuneg.adaptor.web.controller.user
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.oauth
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import org.slf4j.event.Level
import java.net.URI
import com.neptuneg.config.Keycloak as KeycloakConfig
import com.neptuneg.config.Server as ServerConfig

fun Application.installGenerals() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = Level.INFO
    }
}

fun Application.installAuth(keycloakConfig: KeycloakConfig) {
    install(Authentication) {
//        setupOAuth(keycloakConfig)
        setupJWT(keycloakConfig)
    }
}

fun AuthenticationConfig.setupOAuth(keycloakConfig: KeycloakConfig) {
    val keycloakOAuthProvider = OAuthServerSettings.OAuth2ServerSettings(
        name = "keycloak",
        authorizeUrl = keycloakConfig.authorizeUrl,
        accessTokenUrl = keycloakConfig.accessTokenUrl,
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

fun Application.setupRouting() {
    routing {
        sample()
        user()
    }
}

class Ktor(
    private val serverConfig: ServerConfig
) : Server {
    override fun serve() {
        embeddedServer(Netty, port = serverConfig.port) {
            installGenerals()
            installAuth(serverConfig.keycloak)
            setupRouting()
            setupMonitoringRouting()
        }.start(wait = true)
    }
}
