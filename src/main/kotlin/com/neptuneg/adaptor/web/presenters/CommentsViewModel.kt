package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.GetArticleComments200Response
import com.neptuneg.domain.entities.Comment as DomainComment

object CommentsViewModel {
    operator fun invoke(comments: List<DomainComment>) = GetArticleComments200Response(
        comments = comments.map { it.toView() }
    )
}
