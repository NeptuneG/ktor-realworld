package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Pagination
import java.util.*

interface ArticleUseCase {
    data class CreateParam(
        val title: String,
        val description: String,
        val body: String,
        val tags: List<String>
    )
    data class SearchParam(
        val tag: String? = null,
        val authorName: String? = null,
        val favoritedUserName: String? = null,
        val pagination: Pagination = Pagination.default
    )
    data class UpdateParam(
        val title: String? = null,
        val description: String? = null,
        val body: String? = null,
    )

    fun create(authorId: UUID, param: CreateParam): Result<Article>
    fun find(slug: String): Result<Article>
    fun listUserFeed(userId: UUID, pagination: Pagination): Result<List<Article>>
    fun search(param: SearchParam): Result<List<Article>>
    fun update(slug: String, param: UpdateParam): Result<Article>
    fun delete(slug: String): Result<Article>
    fun favoriteArticle(userId: UUID, slug: String): Result<Article>
    fun unfavoriteArticle(userId: UUID, slug: String): Result<Article>
}
