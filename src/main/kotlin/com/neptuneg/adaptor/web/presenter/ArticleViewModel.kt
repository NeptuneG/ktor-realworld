package com.neptuneg.adaptor.web.presenter

import com.neptuneg.autogen.model.Article
import com.neptuneg.autogen.model.CreateArticle201Response
import com.neptuneg.domain.entity.Article as DomainArticle

object ArticleViewModel {
    operator fun invoke(article: DomainArticle) = CreateArticle201Response(article = article.toView())
}

internal fun DomainArticle.toView() = Article(
    slug = slug,
    title = title,
    description = description,
    body = body,
    tagList = tags.map { it.tag }.sorted(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    favorited = favorited,
    favoritesCount = favoritesCount.toInt(),
    author = author.toView()
)
