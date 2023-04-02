package com.neptuneg.adaptor.web.controller

import com.neptuneg.adaptor.web.presenter.ArticleViewModel
import com.neptuneg.adaptor.web.presenter.ArticlesViewModel
import com.neptuneg.autogen.model.CreateArticleRequest
import com.neptuneg.autogen.model.UpdateArticleRequest
import com.neptuneg.domain.entity.Article
import com.neptuneg.domain.entity.Pagination
import com.neptuneg.domain.entity.Profile
import com.neptuneg.domain.entity.Tag
import com.neptuneg.usecase.inputport.ArticleUseCase
import com.neptuneg.usecase.inputport.UserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

fun Route.article() {
    val userUseCase: UserUseCase by inject(UserUseCase::class.java)
    val articleUseCase: ArticleUseCase by inject(ArticleUseCase::class.java)

    route("/articles") {
        authenticate("keycloakJWT", optional = true) {
            get {
                val user = call.accessToken?.let {
                    userUseCase.findByToken(call.accessToken!!).getOrThrow()
                }
                val searchParam = call.request.queryParameters.toSearchParam()
                val articles = articleUseCase.searchArticles(user, searchParam).getOrThrow()
                call.respond(HttpStatusCode.OK, ArticlesViewModel(articles))
            }

            get("/{slug}") {
                val user = call.accessToken?.let {
                    userUseCase.findByToken(call.accessToken!!).getOrThrow()
                }
                val article = articleUseCase.findArticle(user, call.slug).getOrThrow()
                call.respond(HttpStatusCode.OK, ArticleViewModel(article))
            }
        }

        authenticate("keycloakJWT") {
            post {
                val request = call.receive<CreateArticleRequest>()
                val author = userUseCase.findByToken(call.accessToken!!).getOrThrow()
                val article = articleUseCase.createArticle(request.toDomainArticle(author.profile())).getOrThrow()
                call.respond(HttpStatusCode.Created, ArticleViewModel(article))
            }

            get("/feed") {
                val user = userUseCase.findByToken(call.accessToken!!).getOrThrow()
                val pagination = call.request.queryParameters.toPagination()
                val articles = articleUseCase.fetchUserFeed(user, pagination).getOrThrow()
                call.respond(HttpStatusCode.OK, ArticlesViewModel(articles))
            }

            route("/{slug}") {
                put {
                    val param = call.receive<UpdateArticleRequest>().toUpdateParam()
                    val article = articleUseCase.updateArticle(call.slug, param).getOrThrow()
                    call.respond(HttpStatusCode.OK, ArticleViewModel(article))
                }

                delete {
                    val article = articleUseCase.deleteArticle(call.slug).getOrThrow()
                    call.respond(HttpStatusCode.OK, ArticleViewModel(article))
                }

                route("/favorite") {
                    post {
                        val user = userUseCase.findByToken(call.accessToken!!).getOrThrow()
                        val article = articleUseCase.favoriteArticle(user, call.slug).getOrThrow()
                        call.respond(HttpStatusCode.OK, ArticleViewModel(article))
                    }

                    delete {
                        val user = userUseCase.findByToken(call.accessToken!!).getOrThrow()
                        val article = articleUseCase.unfavoriteArticle(user, call.slug).getOrThrow()
                        call.respond(HttpStatusCode.OK, ArticleViewModel(article))
                    }
                }
            }
        }
    }
}

internal val ApplicationCall.slug: String get() = parameters["slug"]
    ?: throw BadRequestException("slug is required")

internal fun CreateArticleRequest.toDomainArticle(author: Profile) = article.let {
    Article(
        title = it.title,
        description = it.description,
        body = it.body,
        tags = it.tagList?.map { tag -> Tag(tag) } ?: emptyList(),
        author = author
    )
}

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

internal fun UpdateArticleRequest.toUpdateParam() = ArticleUseCase.UpdateArticleParam(
    title = article.title,
    description = article.description,
    body = article.body
)
