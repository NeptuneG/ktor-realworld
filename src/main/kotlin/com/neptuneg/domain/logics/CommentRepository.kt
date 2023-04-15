package com.neptuneg.domain.logics

import com.neptuneg.domain.entities.Comment
import java.util.*

interface CommentRepository {
    fun find(id: Int): Result<Comment>
    fun list(articleSlug: String): Result<List<Comment>>
    fun create(authorId: UUID, articleSlug: String, body: String): Result<Comment>
    fun delete(comment: Comment): Result<Unit>
}
