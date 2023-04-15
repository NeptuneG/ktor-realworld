package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.Article
import com.neptuneg.autogen.model.CreateArticle201Response
import java.util.*
import com.neptuneg.domain.entities.Article as DomainArticle

object ArticleViewModel {
    operator fun invoke(article: DomainArticle, userId: UUID? = null) = CreateArticle201Response(article.toView(userId))
}

internal fun DomainArticle.toView(userId: UUID?) = Article(
    slug = slug,
    title = title,
    description = description,
    body = body,
    tagList = tags.map { it.tag }.sorted(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    favorited = userId?.let { favoriterIds.contains(it) } ?: false,
    favoritesCount = favoriterIds.size,
    author = author.toProfile(userId)
)
