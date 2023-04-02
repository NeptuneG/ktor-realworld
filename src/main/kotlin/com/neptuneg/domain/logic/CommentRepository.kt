package com.neptuneg.domain.logic

import com.neptuneg.domain.entity.Comment
import com.neptuneg.domain.entity.User

interface CommentRepository {
    fun getArticleComments(articleSlug: String, user: User?): Result<List<Comment>>
    fun createComment(articleSlug: String, comment: Comment): Result<Comment>
    fun deleteComment(commentId: Int): Result<Comment>
}
