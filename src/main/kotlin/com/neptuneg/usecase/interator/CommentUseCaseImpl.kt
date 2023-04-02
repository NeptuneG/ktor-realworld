package com.neptuneg.usecase.interator

import com.neptuneg.domain.entity.Comment
import com.neptuneg.domain.entity.User
import com.neptuneg.domain.logic.CommentRepository
import com.neptuneg.usecase.inputport.CommentUseCase

class CommentUseCaseImpl(
    private val commentRepository: CommentRepository
): CommentUseCase {
    override fun getArticleComments(articleSlug: String, user: User?): Result<List<Comment>> {
        return commentRepository.getArticleComments(articleSlug, user)
    }

    override fun createComment(articleSlug: String, comment: Comment): Result<Comment> {
        return commentRepository.createComment(articleSlug, comment)
    }

    override fun deleteComment(commentId: Int): Result<Comment> {
        return commentRepository.deleteComment(commentId)
    }
}
