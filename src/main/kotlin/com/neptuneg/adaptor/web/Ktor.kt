package com.neptuneg.adaptor.web

import com.neptuneg.Server
import com.neptuneg.adaptor.web.util.installAuthentication
import com.neptuneg.adaptor.web.util.installCallLogging
import com.neptuneg.adaptor.web.util.installContentNegotiation
import com.neptuneg.adaptor.web.util.installMicrometerMetrics
import com.neptuneg.adaptor.web.util.installRouting
import com.neptuneg.infrastructure.config.ServerConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class Ktor(
    private val serverConfig: ServerConfig
) : Server {
    override fun serve() {
        embeddedServer(Netty, port = serverConfig.port) {
            installCallLogging()
            installContentNegotiation()
            installAuthentication(serverConfig.keycloak)
            installRouting()
            installMicrometerMetrics()
        }.start(wait = true)
    }
}
