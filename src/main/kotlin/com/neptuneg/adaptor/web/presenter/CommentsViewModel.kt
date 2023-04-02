package com.neptuneg.adaptor.web.presenter

import com.neptuneg.autogen.model.GetArticleComments200Response
import com.neptuneg.domain.entity.Comment as DomainComment

object CommentsViewModel {
    operator fun invoke(comments: List<DomainComment>) = GetArticleComments200Response(
        comments = comments.map { it.toView() }
    )
}
