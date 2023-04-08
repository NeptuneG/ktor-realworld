package com.neptuneg.adaptor.web.controllers

import com.neptuneg.adaptor.web.presenters.ProfileViewModel
import com.neptuneg.usecase.inputport.ProfileUseCase
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
        val profileUseCase: ProfileUseCase by inject(ProfileUseCase::class.java)
        val userUseCase: UserUseCase by inject(UserUseCase::class.java)

        authenticate("keycloakJWT", optional = true) {
            get {
                val follower = call.accessToken?.let { userUseCase.findByToken(it).getOrThrow() }
                val followee = userUseCase.findByUsername(call.username).getOrThrow()
                val profile = profileUseCase.get(follower, followee).getOrThrow()
                call.respond(HttpStatusCode.OK, ProfileViewModel(profile))
            }
        }

        authenticate("keycloakJWT") {
            route("/follow") {
                post {
                    val follower = userUseCase.findByToken(call.accessToken!!).getOrThrow()
                    val followee = userUseCase.findByUsername(call.username).getOrThrow()
                    val profile = profileUseCase.follow(follower, followee).getOrThrow()
                    call.respond(HttpStatusCode.OK, ProfileViewModel(profile))
                }

                delete {
                    val follower = userUseCase.findByToken(call.accessToken!!).getOrThrow()
                    val followee = userUseCase.findByUsername(call.username).getOrThrow()
                    val profile = profileUseCase.unfollow(follower, followee).getOrThrow()
                    call.respond(HttpStatusCode.OK, ProfileViewModel(profile))
                }
            }
        }
    }
}

internal val ApplicationCall.username: String get() = parameters["username"]
    ?: throw BadRequestException("username is required")
