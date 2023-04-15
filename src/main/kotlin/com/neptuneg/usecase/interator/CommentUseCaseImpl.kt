package com.neptuneg.usecase.interator

import com.neptuneg.domain.entities.Comment
import com.neptuneg.domain.logics.CommentRepository
import com.neptuneg.usecase.inputport.CommentUseCase
import java.util.*

class CommentUseCaseImpl(
    private val commentRepository: CommentRepository
) : CommentUseCase {
    override fun list(articleSlug: String): Result<List<Comment>> {
        return commentRepository.list(articleSlug)
    }

    override fun create(authorId: UUID, articleSlug: String, body: String): Result<Comment> {
        return commentRepository.create(authorId, articleSlug, body)
    }

    override fun delete(id: Int): Result<Comment> {
        return commentRepository.find(id).onSuccess {
            commentRepository.delete(it).getOrThrow()
        }
    }
}
