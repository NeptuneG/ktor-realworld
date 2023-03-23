package com.neptuneg.adaptor.web.controller

import com.auth0.jwt.interfaces.Payload
import com.neptuneg.adaptor.web.presenter.buildUserViewModel
import com.neptuneg.autogen.model.CreateUserRequest
import com.neptuneg.autogen.model.LoginRequest
import com.neptuneg.autogen.model.UpdateUser
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
            val request = call.receive<CreateUserRequest>()
            val user = userUseCase.create(
                User(email = request.user.email, username = request.user.username),
                request.user.password
            ).getOrThrow()
            val token = userUseCase.requestToken(request.user.email, request.user.password).getOrThrow()

            call.respond(
                HttpStatusCode.Created,
                buildUserViewModel(user, token)
            )
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val token = userUseCase.requestToken(request.user.email, request.user.password).getOrThrow()
            val user = userUseCase.read(token).getOrThrow()
            call.respond(
                HttpStatusCode.OK,
                buildUserViewModel(user, token)
            )
        }

        authenticate("keycloakJWT") {
            get {
                val token = call.accessToken!!
                val user = userUseCase.read(token).getOrThrow()
                call.respond(
                    HttpStatusCode.OK,
                    buildUserViewModel(user, token)
                )
            }

            put {
                val updateUser = call.receive<UpdateUser>()
                val userId = call.userId
                userUseCase.update(userId, UserUseCase.UserAttributes(
                    email = updateUser.email,
                    password = updateUser.password,
                    username = updateUser.username,
                    bio = updateUser.bio,
                    image = updateUser.image
                )).onSuccess {
                    val token = call.accessToken!!
                    val user = userUseCase.read(token).getOrThrow()
                    call.respond(
                        HttpStatusCode.OK,
                        buildUserViewModel(user, token)
                    )
                }.onFailure {
                    call.respond(HttpStatusCode.UnprocessableEntity)
                }
            }
        }
    }
}

val ApplicationCall.accessToken: String? get() = request.header("Authorization")?.removePrefix("Token ")
val ApplicationCall.payload: Payload get() = principal<JWTPrincipal>()!!.payload
val ApplicationCall.userId: String get() = payload.subject
