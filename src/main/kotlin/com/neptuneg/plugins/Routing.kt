package com.neptuneg.plugins

import com.neptuneg.sample.Runner
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import io.ktor.server.response.respondText
import io.ktor.server.application.Application
import io.ktor.server.application.call
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val service by inject<Runner>()
    routing {
        get("/") {
            call.respondText(service.call())
        }
    }
}
