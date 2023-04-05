package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Pagination
import com.neptuneg.domain.entities.User

interface ArticleUseCase {
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

    fun createArticle(article: Article): Result<Article>
    fun findArticle(user: User?, slug: String): Result<Article>
    fun fetchUserFeed(user: User, pagination: Pagination): Result<List<Article>>
    fun searchArticles(user: User?, param: SearchParam): Result<List<Article>>
    fun updateArticle(slug: String, param: UpdateArticleParam): Result<Article>
    fun deleteArticle(slug: String): Result<Article>
    fun favoriteArticle(user: User, slug: String): Result<Article>
    fun unfavoriteArticle(user: User, slug: String): Result<Article>
}
