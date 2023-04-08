package com.neptuneg.adaptor.web.utils

import com.neptuneg.adaptor.web.controllers.user
import com.neptuneg.adaptor.web.controllers.profile
import com.neptuneg.adaptor.web.controllers.article
import com.neptuneg.adaptor.web.controllers.comment
import com.neptuneg.adaptor.web.controllers.tag
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.routing.route

fun Application.installRouting() {
    routing {
        route("/api") {
            user()
            profile()
            article()
            comment()
            tag()
        }
    }
}
