package com.neptuneg.adaptor.web.controllers

import com.neptuneg.adaptor.web.presenters.ProfileViewModel
import com.neptuneg.usecase.inputport.UserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.java.KoinJavaComponent.inject

fun Route.profile() {
    route("/profiles/{username}") {
        val userUseCase: UserUseCase by inject(UserUseCase::class.java)

        authenticate("keycloakJWT", optional = true) {
            get {
                val visitorId = call.userId
                val user = userUseCase.find(call.username).getOrThrow()
                call.respond(HttpStatusCode.OK, ProfileViewModel(user, visitorId))
            }
        }

        authenticate("keycloakJWT") {
            route("/follow") {
                post {
                    val visitorId = call.userId!!
                    val followingUser = userUseCase.follow(visitorId, call.username).getOrThrow()
                    call.respond(HttpStatusCode.OK, ProfileViewModel(followingUser, visitorId))
                }

                delete {
                    val visitorId = call.userId!!
                    val followingUser = userUseCase.unfollow(visitorId, call.username).getOrThrow()
                    call.respond(HttpStatusCode.OK, ProfileViewModel(followingUser, visitorId))
                }
            }
        }
    }
}

internal val ApplicationCall.username: String get() = parameters["username"]
    ?: throw BadRequestException("username is required")
