package com.neptuneg.adaptor.web.utils

import com.neptuneg.infrastructure.exceptions.ConflictException
import com.neptuneg.infrastructure.exceptions.NotFoundException
import com.neptuneg.infrastructure.exceptions.UnexpectedException
import com.neptuneg.infrastructure.exceptions.UnprocessableEntityException
import com.neptuneg.infrastructure.exceptions.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.CallFailed
import io.ktor.server.application.install
import io.ktor.server.response.respond

fun Application.installErrorHandler() {
    val errorHandler = createApplicationPlugin("ErrorHandler") {
        on(CallFailed) { call, error ->
            val (status, message) = error.toResponseByStatusCode()
            call.respond(status, message)
        }
    }
    install(errorHandler)
}

internal fun Throwable.toResponseByStatusCode(): Pair<HttpStatusCode, ErrorResponse> {
    return when (this) {
        is ValidationException -> HttpStatusCode.UnprocessableEntity
        is UnexpectedException -> HttpStatusCode.InternalServerError
        is NotFoundException -> HttpStatusCode.NotFound
        is UnprocessableEntityException -> HttpStatusCode.UnprocessableEntity
        is ConflictException -> HttpStatusCode.UnprocessableEntity
        else -> HttpStatusCode.InternalServerError
    }.let { it to ErrorResponse(message) }
}

internal data class ErrorResponse(val errors: Err) {
    data class Err(val body: List<String>)
    constructor(message: String?) : this(
        Err(message?.let { listOf(it) } ?: emptyList())
    )
}
