package com.neptuneg.adaptor.web.controller

import com.neptuneg.adaptor.web.presenter.CommentViewModel
import com.neptuneg.adaptor.web.presenter.CommentsViewModel
import com.neptuneg.autogen.model.CreateArticleCommentRequest
import com.neptuneg.usecase.inputport.CommentUseCase
import com.neptuneg.usecase.inputport.UserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

fun Route.comment() {
    val userUseCase: UserUseCase by inject(UserUseCase::class.java)
    val commentUseCase: CommentUseCase by inject(CommentUseCase::class.java)

    route("/articles/{slug}") {
        route("/comments") {
            authenticate("keycloakJWT", optional = true) {
                get {
                    val user = call.accessToken?.let { userUseCase.findByToken(it).getOrThrow() }
                    val comments = commentUseCase.getArticleComments(call.slug, user).getOrThrow()
                    call.respond(HttpStatusCode.OK, CommentsViewModel(comments))
                }
            }

            authenticate("keycloakJWT") {
                post {
                    val body = call.receive<CreateArticleCommentRequest>().comment.body
                    val author = userUseCase.findByToken(call.accessToken!!).getOrThrow()
                    val comment = commentUseCase.createComment(author, body).getOrThrow()
                    call.respond(HttpStatusCode.OK, CommentViewModel(comment))
                }
            }

            authenticate("keycloakJWT") {
                delete("/{commentId}") {
                    val comment = commentUseCase.deleteComment(call.commentId).getOrThrow()
                    call.respond(HttpStatusCode.OK, CommentViewModel(comment))
                }
            }
        }
    }
}

internal val ApplicationCall.commentId: String get() = parameters["commentId"]
    ?: throw BadRequestException("commentId is required")
