package com.neptuneg.adaptor.web.controller

import com.neptuneg.domain.entity.User
import com.neptuneg.usecase.inputport.UserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Routing.user() {
    route("/users") {
        val userUseCase by inject<UserUseCase>()
        // Create user
        post {
            val user = call.receive<User>()
            val id = userUseCase.create(user)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user
        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userUseCase.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<User>()
            userUseCase.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        // Delete user
        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userUseCase.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
