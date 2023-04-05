package com.neptuneg.adaptor.web.utils

import com.neptuneg.adaptor.web.controllers.*
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.routing.route

fun Application.installRouting() {
    routing {
        sample()
        route("/api") {
            user()
            profile()
            article()
            comment()
            tag()
        }
    }
}
