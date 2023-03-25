package com.neptuneg.adaptor.web.util

import com.neptuneg.adaptor.web.controller.profile
import com.neptuneg.adaptor.web.controller.sample
import com.neptuneg.adaptor.web.controller.user
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.routing.route

fun Application.installRouting() {
    routing {
        sample()
        route("/api") {
            user()
            profile()
        }
    }
}
