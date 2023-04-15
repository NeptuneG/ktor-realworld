package com.neptuneg.adaptor.web.controllers

import com.auth0.jwt.interfaces.Payload
import com.neptuneg.adaptor.web.presenters.UserViewModel
import com.neptuneg.autogen.model.CreateUserRequest
import com.neptuneg.autogen.model.LoginRequest
import com.neptuneg.autogen.model.UpdateCurrentUserRequest
import com.neptuneg.autogen.model.UpdateUser
import com.neptuneg.usecase.inputport.UserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.java.KoinJavaComponent.inject
import java.util.*
import kotlin.reflect.full.memberProperties

@Suppress("ThrowsCount")
fun Route.user() {
    val userUseCase: UserUseCase by inject(UserUseCase::class.java)

    route("/users") {
        post {
            val request = call.receive<CreateUserRequest>().user
            val user = userUseCase.register(request.username, request.email, request.password).getOrThrow()
            call.respond(HttpStatusCode.Created, UserViewModel(user))
        }

        post("/login") {
            val request = call.receive<LoginRequest>().user
            val user = userUseCase.login(request.email, request.password).getOrThrow()
            call.respond(HttpStatusCode.OK, UserViewModel(user))
        }
    }

    route("/user") {
        authenticate("keycloakJWT") {
            get {
                val user = userUseCase.find(call.userId!!).getOrThrow()
                call.respond(HttpStatusCode.OK, UserViewModel(user, call.accessToken!!))
            }

            put {
                val userAttributes = call.receive<UpdateCurrentUserRequest>().user.toMap()
                val user = userUseCase.updateProfile(call.userId!!, userAttributes).getOrThrow()
                call.respond(HttpStatusCode.OK, UserViewModel(user, call.accessToken!!))
            }
        }
    }
}

internal val ApplicationCall.accessToken: String? get() = request.header("Authorization")?.removePrefix("Token ")
internal val ApplicationCall.payload: Payload? get() = principal<JWTPrincipal>()?.payload
internal val ApplicationCall.userId: UUID? get() = payload?.let { UUID.fromString(it.subject) }
internal fun UpdateUser.toMap(): Map<String, String?> =
    UpdateUser::class.memberProperties.associate { it.name to it.get(this) as String? }
