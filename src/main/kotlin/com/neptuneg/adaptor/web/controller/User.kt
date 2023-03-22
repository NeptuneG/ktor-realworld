package com.neptuneg.adaptor.web.controller

import com.auth0.jwt.interfaces.Payload
import com.neptuneg.domain.entity.User
import com.neptuneg.usecase.inputport.UserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

@Suppress("ThrowsCount")
fun Routing.user() {
    route("/users") {
        val userUseCase by inject<UserUseCase>()

        post {
            val user = call.receive<User>()
            val token = userUseCase.create(user)
            val response = mapOf(
                "user" to mapOf(
                    "email" to user.email,
                    "token" to token,
                    "username" to user.username,
                    "bio" to user.bio,
                    "image" to user.image,
                )
            )
            call.respond(HttpStatusCode.Created, response)
        }

        post("/login") {
            val user = call.receive<User>()
            val token = userUseCase.requestToken(user)
            val response = mapOf(
                "user" to mapOf(
                    "email" to user.email,
                    "token" to token,
                    "username" to user.username,
                    "bio" to user.bio,
                    "image" to user.image,
                )
            )
            call.respond(HttpStatusCode.Created, response)
        }

        authenticate("keycloakJWT") {
            get {
                val payload = call.payload
                val token = call.accessToken!!
                val user = mapOf(
                    "user" to mapOf(
                        "email" to payload.getClaim("email").asString(),
                        "token" to token,
                        "username" to payload.getClaim("preferred_username").asString(),
                        "bio" to payload.getClaim("bio").asString(),
                        "image" to payload.getClaim("image").asString(),
                    )
                )
                call.respond(HttpStatusCode.OK, user)
            }

            put {
                val user = call.receive<User>()
                val userId = call.userId
                userUseCase.update(user, userId)
                call.respond(HttpStatusCode.OK, user)
            }
        }
    }
}

val ApplicationCall.accessToken: String? get() = request.header("Authorization")?.removePrefix("Token ")
val ApplicationCall.payload: Payload get() = principal<JWTPrincipal>()!!.payload
val ApplicationCall.userId: String get() = payload.subject