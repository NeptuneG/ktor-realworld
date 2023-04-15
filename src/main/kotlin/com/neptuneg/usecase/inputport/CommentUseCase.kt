package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entities.Comment
import java.util.*

interface CommentUseCase {
    fun list(articleSlug: String): Result<List<Comment>>
    fun create(authorId: UUID, articleSlug: String, body: String): Result<Comment>
    fun delete(id: Int): Result<Comment>
}
