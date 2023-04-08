package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entities.Comment
import com.neptuneg.domain.entities.User

interface CommentUseCase {
    fun getArticleComments(articleSlug: String, user: User?): Result<List<Comment>>
    fun createComment(articleSlug: String, comment: Comment): Result<Comment>
    fun deleteComment(commentId: Int): Result<Comment>
}
