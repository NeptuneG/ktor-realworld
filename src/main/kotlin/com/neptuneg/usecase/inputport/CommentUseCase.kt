package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.Comment
import com.neptuneg.domain.entity.User

interface CommentUseCase {
    fun getArticleComments(slug: String, user: User?): Result<List<Comment>>
    fun createComment(author: User, body: String): Result<Comment>
    fun deleteComment(commentId: String): Result<Comment>
}
