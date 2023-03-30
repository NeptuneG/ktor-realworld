package com.neptuneg.adaptor.web.controller

import com.auth0.jwt.interfaces.Payload
import com.neptuneg.adaptor.web.presenter.UserViewModel
import com.neptuneg.autogen.model.CreateUserRequest
import com.neptuneg.autogen.model.LoginRequest
import com.neptuneg.autogen.model.UpdateUser
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
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.reflect.full.memberProperties
import org.koin.java.KoinJavaComponent.inject

@Suppress("ThrowsCount")
fun Route.user() {
    val userUseCase: UserUseCase by inject(UserUseCase::class.java)

    route("/users") {
        post {
            val request = call.receive<CreateUserRequest>()
            val user = userUseCase.create(request.user.username, request.user.email, request.user.password).getOrThrow()
            val token = userUseCase.requestToken(request.user.email, request.user.password).getOrThrow()

            call.respond(
                HttpStatusCode.Created,
                UserViewModel(user, token)
            )
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val token = userUseCase.requestToken(request.user.email, request.user.password).getOrThrow()
            val user = userUseCase.findByToken(token).getOrThrow()
            call.respond(HttpStatusCode.OK, UserViewModel(user, token))
        }
    }


    route("/user") {
        authenticate("keycloakJWT") {
            get {
                val token = call.accessToken!!
                val user = userUseCase.findByToken(token).getOrThrow()
                call.respond(HttpStatusCode.OK, UserViewModel(user, token))
            }

            put {
                val userId = call.payload.subject
                val userAttributes = call.receive<UpdateUser>().toMap()
                userUseCase.update(userId, userAttributes).onSuccess {
                    val token = call.accessToken!!
                    val user = userUseCase.findByToken(token).getOrThrow()
                    call.respond(HttpStatusCode.OK, UserViewModel(user, token))
                }.onFailure {
                    call.respond(HttpStatusCode.UnprocessableEntity)
                }
            }
        }
    }
}

internal val ApplicationCall.accessToken: String? get() = request.header("Authorization")?.removePrefix("Token ")
internal val ApplicationCall.payload: Payload get() = principal<JWTPrincipal>()!!.payload
internal fun UpdateUser.toMap(): Map<String, String?> =
    UpdateUser::class.memberProperties.associate { it.name to it.get(this) as String? }
