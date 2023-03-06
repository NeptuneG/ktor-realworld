package com.neptuneg.adaptor.web.controller

import com.neptuneg.usecase.inputport.Sample
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.sample() {
    route("/") {
        val sample by inject<Sample>()
        get {
            call.respond(mapOf("message" to sample.foobar()))
        }
    }
}
