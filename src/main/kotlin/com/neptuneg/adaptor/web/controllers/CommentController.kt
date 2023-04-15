package com.neptuneg.adaptor.web.controllers

import com.neptuneg.adaptor.web.presenters.CommentViewModel
import com.neptuneg.adaptor.web.presenters.CommentsViewModel
import com.neptuneg.adaptor.web.utils.authorizeOnComment
import com.neptuneg.autogen.model.CreateArticleCommentRequest
import com.neptuneg.usecase.inputport.CommentUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.java.KoinJavaComponent.inject

fun Route.comment() {
    val commentUseCase: CommentUseCase by inject(CommentUseCase::class.java)

    route("/articles/{slug}") {
        route("/comments") {
            authenticate("keycloakJWT", optional = true) {
                get {
                    val comments = commentUseCase.list(call.slug).getOrThrow()
                    call.respond(HttpStatusCode.OK, CommentsViewModel(comments, call.userId))
                }
            }

            authenticate("keycloakJWT") {
                post {
                    val userId = call.userId!!
                    val body = call.receive<CreateArticleCommentRequest>().comment.body
                    val comment = commentUseCase.create(userId, call.slug, body).getOrThrow()
                    call.respond(HttpStatusCode.OK, CommentViewModel(comment))
                }
            }

            authenticate("keycloakJWT") {
                route("/{commentId}") {
                    authorizeOnComment {
                        delete {
                            val comment = commentUseCase.delete(call.commentId).getOrThrow()
                            call.respond(HttpStatusCode.OK, CommentViewModel(comment))
                        }
                    }
                }
            }
        }
    }
}

internal val ApplicationCall.commentId: Int get() = parameters["commentId"]?.toInt()
    ?: throw BadRequestException("commentId is required")
