package com.neptuneg.adaptor.web

import com.neptuneg.adaptor.web.controller.setupMonitoringRouting
import com.neptuneg.adaptor.web.controller.sample
import com.neptuneg.adaptor.web.controller.user
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.slf4j.event.Level

object Server {
    fun Application.installGenerals() {
        install(ContentNegotiation) {
            json()
        }
        install(CallLogging) {
            level = Level.INFO
        }
    }

    private fun Application.setupRouting() {
        routing {
            sample()
            user()
        }
    }

    fun serve(port: Int) {
        embeddedServer(Netty, port = port) {
            installGenerals()
            insertKoins()
            setupRouting()
            setupMonitoringRouting()
        }.start(wait = true)
    }
}
