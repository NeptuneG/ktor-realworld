package com.neptuneg.adaptor.web.controllers

import com.neptuneg.adaptor.web.presenters.CommentViewModel
import com.neptuneg.adaptor.web.presenters.CommentsViewModel
import com.neptuneg.autogen.model.CreateArticleCommentRequest
import com.neptuneg.domain.entities.Comment
import com.neptuneg.usecase.inputport.CommentUseCase
import com.neptuneg.usecase.inputport.UserUseCase
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
                    val comment = commentUseCase.createComment(call.slug, Comment(body, author.profile())).getOrThrow()
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

internal val ApplicationCall.commentId: Int get() = parameters["commentId"]?.toInt()
    ?: throw BadRequestException("commentId is required")
