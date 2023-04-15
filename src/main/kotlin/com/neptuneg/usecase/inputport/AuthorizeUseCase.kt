package com.neptuneg.usecase.inputport

import java.util.*

interface AuthorizeUseCase {
    fun isPermittedToArticle(userId: UUID, articleSlug: String): Result<Boolean>
    fun isPermittedToComment(userId: UUID, commentId: Int): Result<Boolean>
}
