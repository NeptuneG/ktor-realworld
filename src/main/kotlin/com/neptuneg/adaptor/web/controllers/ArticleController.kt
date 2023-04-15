package com.neptuneg.adaptor.web.controllers

import com.neptuneg.adaptor.web.presenters.ArticleViewModel
import com.neptuneg.adaptor.web.presenters.ArticlesViewModel
import com.neptuneg.adaptor.web.utils.authorizeOnArticle
import com.neptuneg.autogen.model.CreateArticleRequest
import com.neptuneg.autogen.model.NewArticle
import com.neptuneg.autogen.model.UpdateArticleRequest
import com.neptuneg.domain.entities.Pagination
import com.neptuneg.usecase.inputport.ArticleUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
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
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.java.KoinJavaComponent.inject

@Suppress("LongMethod")
fun Route.article() {
    val articleUseCase: ArticleUseCase by inject(ArticleUseCase::class.java)

    route("/articles") {
        authenticate("keycloakJWT", optional = true) {
            get {
                val searchParam = call.request.queryParameters.toSearchParam()
                val articles = articleUseCase.search(searchParam).getOrThrow()
                call.respond(HttpStatusCode.OK, ArticlesViewModel(articles, call.userId))
            }

            get("/{slug}") {
                val article = articleUseCase.find(call.slug).getOrThrow()
                call.respond(HttpStatusCode.OK, ArticleViewModel(article, call.userId))
            }
        }

        authenticate("keycloakJWT") {
            post {
                val request = call.receive<CreateArticleRequest>().article
                val article = articleUseCase.create(call.userId!!, request.toCreateParam()).getOrThrow()
                call.respond(HttpStatusCode.Created, ArticleViewModel(article))
            }

            get("/feed") {
                val userId = call.userId!!
                val pagination = call.request.queryParameters.toPagination()
                val articles = articleUseCase.listUserFeed(userId, pagination).getOrThrow()
                call.respond(HttpStatusCode.OK, ArticlesViewModel(articles, userId))
            }

            route("/{slug}") {
                authorizeOnArticle {
                    put {
                        val param = call.receive<UpdateArticleRequest>().toUpdateParam()
                        val article = articleUseCase.update(call.slug, param).getOrThrow()
                        call.respond(HttpStatusCode.OK, ArticleViewModel(article))
                    }

                    delete {
                        val article = articleUseCase.delete(call.slug).getOrThrow()
                        call.respond(HttpStatusCode.OK, ArticleViewModel(article))
                    }
                }

                route("/favorite") {
                    post {
                        val userId = call.userId!!
                        val article = articleUseCase.favoriteArticle(userId, call.slug).getOrThrow()
                        call.respond(HttpStatusCode.OK, ArticleViewModel(article, userId))
                    }

                    delete {
                        val userId = call.userId!!
                        val article = articleUseCase.unfavoriteArticle(userId, call.slug).getOrThrow()
                        call.respond(HttpStatusCode.OK, ArticleViewModel(article, userId))
                    }
                }
            }
        }
    }
}

internal val ApplicationCall.slug: String get() = parameters["slug"]
    ?: throw BadRequestException("slug is required")

internal fun NewArticle.toCreateParam() =
    ArticleUseCase.CreateParam(title, description, body, tagList ?: emptyList())

internal fun Parameters.toPagination() = Pagination(
    offset = this["offset"]?.toLong() ?: Pagination.defaultOffset,
    limit = this["limit"]?.toInt() ?: Pagination.defaultLimit
)

internal fun Parameters.toSearchParam() = ArticleUseCase.SearchParam(
    tag = this["tag"],
    authorName = this["author"],
    favoritedUserName = this["favorited"],
    pagination = toPagination()
)

internal fun UpdateArticleRequest.toUpdateParam() =
    ArticleUseCase.UpdateParam(article.title, article.description, article.body)
