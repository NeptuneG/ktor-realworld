package com.neptuneg.adaptor.web

import com.neptuneg.Server
import com.neptuneg.adaptor.web.utils.installAuthentication
import com.neptuneg.adaptor.web.utils.installCallLogging
import com.neptuneg.adaptor.web.utils.installContentNegotiation
import com.neptuneg.adaptor.web.utils.installMicrometerMetrics
import com.neptuneg.adaptor.web.utils.installRouting
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
