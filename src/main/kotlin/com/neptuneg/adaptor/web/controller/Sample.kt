package com.neptuneg.adaptor.web.controller

import com.neptuneg.usecase.inputport.Sample
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.java.KoinJavaComponent.getKoin

fun Routing.sample() {
    val sample: Sample = getKoin().get()

    route("/") {
        get {
            call.respond(sample.foobar())
        }
    }

    authenticate("keycloakJWT") {
        get("/hello") {
            call.respond(sample.foobar())
        }
    }
}
