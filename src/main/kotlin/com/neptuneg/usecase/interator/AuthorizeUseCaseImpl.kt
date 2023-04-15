package com.neptuneg.usecase.interator

import com.neptuneg.domain.logics.ArticleRepository
import com.neptuneg.domain.logics.CommentRepository
import com.neptuneg.usecase.inputport.AuthorizeUseCase
import java.util.*

class AuthorizeUseCaseImpl(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
) : AuthorizeUseCase {
    override fun isPermittedToArticle(userId: UUID, articleSlug: String): Result<Boolean> {
        return articleRepository.find(articleSlug).map { it.author.id == userId }
    }

    override fun isPermittedToComment(userId: UUID, commentId: Int): Result<Boolean> {
        return commentRepository.find(commentId).map { it.author.id == userId }
    }
}
