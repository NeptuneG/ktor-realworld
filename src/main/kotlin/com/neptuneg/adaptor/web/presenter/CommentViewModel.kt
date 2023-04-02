package com.neptuneg.adaptor.web.presenter

import com.neptuneg.autogen.model.Comment
import com.neptuneg.autogen.model.CreateArticleComment200Response
import com.neptuneg.domain.entity.Comment as DomainComment

object CommentViewModel {
    operator fun invoke(comment: DomainComment) = CreateArticleComment200Response(
        comment = comment.toView()
    )
}

internal fun DomainComment.toView() = Comment(
    id = id!!,
    createdAt = createdAt,
    updatedAt = updatedAt,
    body = body,
    author = author.toView()
)
