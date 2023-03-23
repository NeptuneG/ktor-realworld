package com.neptuneg.adaptor.web

import com.auth0.jwk.UrlJwkProvider
import com.neptuneg.adaptor.web.controller.sample
import com.neptuneg.adaptor.web.controller.setupMonitoringRouting
import com.neptuneg.adaptor.web.controller.user
import com.squareup.moshi.Moshi
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.http.*
import io.ktor.serialization.*
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
import com.neptuneg.domain.entity.serializer.Serializer
import io.ktor.http.content.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.event.Level
import java.net.URI
import com.neptuneg.config.Keycloak as KeycloakConfig
import com.neptuneg.config.Server as ServerConfig

fun Application.installGenerals() {
    install(ContentNegotiation) {
        moshi()
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

fun Application.setupRouting() {
    routing {
        sample()
        user()
    }
}
fun Configuration.moshi(
    contentType: ContentType = ContentType.Application.Json,
    block: Moshi.Builder.() -> Unit = {}
) {
    val builder = Serializer.moshiBuilder.apply(block)
    val converter = MoshiConverter(builder.build())
    register(contentType, converter)
}

class MoshiConverter(private val moshi: Moshi) : ContentConverter {
    override suspend fun serializeNullable(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?
    ): OutgoingContent? {
        if (value == null) {
            return null
        }
        return TextContent(
            moshi.adapter(value.javaClass).toJson(value),
            contentType.withCharsetIfNeeded(charset)
        )
    }

    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        return withContext(Dispatchers.IO) {
            val body = content.toInputStream().reader(charset).buffered().use { it.readText() }
            if (body.isEmpty()) throw JsonConvertException("Empty Json data")
            moshi.adapter(typeInfo.type.java).fromJson(body)
        }
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
