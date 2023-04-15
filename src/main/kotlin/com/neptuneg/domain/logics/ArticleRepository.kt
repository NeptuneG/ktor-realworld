package com.neptuneg.domain.logics

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Pagination
import java.util.*

interface ArticleRepository {
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

    fun find(slug: String): Result<Article>
    fun listUserFeed(userId: UUID, pagination: Pagination = Pagination.default): Result<List<Article>>
    fun create(authorId: UUID, param: CreateParam): Result<Article>
    fun update(article: Article): Result<Unit>
    fun updateFavoriterIds(article: Article): Result<Unit>
    fun delete(article: Article): Result<Unit>
    fun search(param: SearchParam): Result<List<Article>>
}
