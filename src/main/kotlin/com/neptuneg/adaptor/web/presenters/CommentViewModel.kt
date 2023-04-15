package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.Comment
import com.neptuneg.autogen.model.CreateArticleComment200Response
import java.util.*
import com.neptuneg.domain.entities.Comment as DomainComment

object CommentViewModel {
    operator fun invoke(comment: DomainComment, userId: UUID? = null) = CreateArticleComment200Response(
        comment = comment.toView(userId)
    )
}

internal fun DomainComment.toView(userId: UUID?) = Comment(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    body = body,
    author = author.toProfile(userId)
)
