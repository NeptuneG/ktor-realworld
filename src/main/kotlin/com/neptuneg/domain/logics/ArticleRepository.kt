package com.neptuneg.domain.logics

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Pagination
import com.neptuneg.domain.entities.User

interface ArticleRepository {
    data class SearchParam(
        val tag: String? = null,
        val authorName: String? = null,
        val favoritedUserName: String? = null,
        val pagination: Pagination = Pagination.default
    )
    data class UpdateArticleParam(
        val title: String? = null,
        val description: String? = null,
        val body: String? = null,
    )

    fun findBySlug(slug: String, user: User? = null): Result<Article>
    fun fetchUserFeed(user: User, pagination: Pagination = Pagination.default): Result<List<Article>>
    fun create(article: Article): Result<Article>
    fun updateBySlug(slug: String, param: UpdateArticleParam): Result<Article>
    fun deleteBySlug(slug: String): Result<Article>
    fun search(param: SearchParam, user: User? = null): Result<List<Article>>
}
