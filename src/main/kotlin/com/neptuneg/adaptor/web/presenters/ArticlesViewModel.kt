package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.GetArticlesFeed200Response
import java.util.*
import com.neptuneg.domain.entities.Article as DomainArticle

object ArticlesViewModel {
    operator fun invoke(articles: List<DomainArticle>, userId: UUID?) = GetArticlesFeed200Response(
        articles = articles.map { it.toView(userId) },
        articlesCount = articles.size
    )
}
