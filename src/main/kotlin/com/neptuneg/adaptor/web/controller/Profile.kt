package com.neptuneg.adaptor.web.controller

import com.neptuneg.adaptor.web.presenter.ProfileViewModel
import com.neptuneg.usecase.inputport.ProfileUseCase
import com.neptuneg.usecase.inputport.UserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.profile() {
    route("/profiles/{username}") {
        val profileUseCase by inject<ProfileUseCase>()
        val userUseCase by inject<UserUseCase>()

        authenticate("keycloakJWT", optional = true) {
            get {
                val follower = call.accessToken?.let { userUseCase.getByToken(it).getOrThrow() }
                val followee = userUseCase.getByUsername(call.username).getOrThrow()
                val profile = profileUseCase.get(follower, followee).getOrThrow()
                call.respond(HttpStatusCode.OK, ProfileViewModel(profile))
            }
        }

        authenticate("keycloakJWT") {
            route("/follow") {
                post {
                    val follower = userUseCase.getByToken(call.accessToken!!).getOrThrow()
                    val followee = userUseCase.getByUsername(call.username).getOrThrow()
                    val profile = profileUseCase.follow(follower, followee).getOrThrow()
                    call.respond(HttpStatusCode.OK, ProfileViewModel(profile))
                }

                delete {
                    val follower = userUseCase.getByToken(call.accessToken!!).getOrThrow()
                    val followee = userUseCase.getByUsername(call.username).getOrThrow()
                    val profile = profileUseCase.unfollow(follower, followee).getOrThrow()
                    call.respond(HttpStatusCode.OK, ProfileViewModel(profile))
                }
            }
        }
    }
}

internal val ApplicationCall.username: String get() = parameters["username"]
    ?: throw BadRequestException("username is required")
