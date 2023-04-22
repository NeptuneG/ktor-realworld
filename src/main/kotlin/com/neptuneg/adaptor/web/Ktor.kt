package com.neptuneg.adaptor.web

import com.neptuneg.Server
import com.neptuneg.adaptor.web.utils.installAuthentication
import com.neptuneg.adaptor.web.utils.installCORS
import com.neptuneg.adaptor.web.utils.installCallLogging
import com.neptuneg.adaptor.web.utils.installContentNegotiation
import com.neptuneg.adaptor.web.utils.installErrorHandler
import com.neptuneg.adaptor.web.utils.installRouting
import com.neptuneg.infrastructure.config.ServerConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Ktor(
    private val serverConfig: ServerConfig
) : Server {
    override fun serve() {
        embeddedServer(Netty, port = serverConfig.port) {
            installCORS()
            installCallLogging()
            installContentNegotiation()
            installAuthentication(serverConfig.keycloak)
            installRouting()
            installErrorHandler()
        }.start(wait = true)
    }
}
