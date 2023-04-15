package com.neptuneg.adaptor.web.utils

import com.neptuneg.adaptor.web.controllers.commentId
import com.neptuneg.adaptor.web.controllers.slug
import com.neptuneg.adaptor.web.controllers.userId
import com.neptuneg.infrastructure.exceptions.UnAuthorizedException
import com.neptuneg.usecase.inputport.AuthorizeUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.RouteScopedPlugin
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

val ArticleAuthorizationInterpreter = createRouteScopedPlugin("ArticleAuthorizationInterpreter") {
    val useCase: AuthorizeUseCase = getKoin().get()

    fun authorize(userId: UUID, articleSlug: String): Result<Unit> {
        return runCatching {
            val isPermitted = useCase.isPermittedToArticle(userId, articleSlug).getOrThrow()
            if (!isPermitted) throw UnAuthorizedException("The article is not belonging to the user")
        }
    }

    onCallReceive { call ->
        runCatching {
            authorize(call.userId!!, call.slug).getOrThrow()
        }.onFailure {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}

val CommentAuthorizationInterpreter = createRouteScopedPlugin("CommentAuthorizationInterpreter") {
    val useCase: AuthorizeUseCase = getKoin().get()

    fun authorize(userId: UUID, commentId: Int): Result<Unit> {
        return runCatching {
            val isPermitted = useCase.isPermittedToComment(userId, commentId).getOrThrow()
            if (!isPermitted) throw UnAuthorizedException("The comment is not belonging to the user")
        }
    }

    onCallReceive { call ->
        runCatching {
            authorize(call.userId!!, call.commentId).getOrThrow()
        }.onFailure {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}

fun Route.authorize(interpreter: RouteScopedPlugin<Unit>, build: Route.() -> Unit): Route {
    val route = createChild(TransparentRouteSelector())
    route.install(interpreter)
    route.build()
    return route
}

fun Route.authorizeOnArticle(build: Route.() -> Unit): Route {
    return authorize(ArticleAuthorizationInterpreter) {
        build()
    }
}

fun Route.authorizeOnComment(build: Route.() -> Unit): Route {
    return authorize(CommentAuthorizationInterpreter) {
        build()
    }
}
