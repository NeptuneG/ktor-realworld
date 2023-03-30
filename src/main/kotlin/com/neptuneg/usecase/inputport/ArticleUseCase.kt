package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.Article
import com.neptuneg.domain.entity.Profile
import com.neptuneg.domain.entity.User

interface ArticleUseCase {
    data class SearchParam(
        val tag: String? = null,
        val authorName: String? = null,
        val favoritedUserName: String? = null,
        val pagination: PaginationParam = PaginationParam.default
    )
    data class PaginationParam(
        val offset: Int? = defaultOffset,
        val limit: Int? = defaultLimit,
    ) {
        companion object {
            const val defaultOffset = 0
            const val defaultLimit = 20
            val default = PaginationParam(defaultOffset, defaultLimit)
        }
    }
    data class UpdateArticleParam(
        val title: String? = null,
        val description: String? = null,
        val body: String? = null,
    )

    fun createArticle(article: Article): Result<Article>
    fun findArticle(slug: String): Result<Article>
    fun findArticlesByAuthors(authors: List<Profile>, pagination: PaginationParam): Result<List<Article>>
    fun searchArticles(param: SearchParam): Result<List<Article>>
    fun updateArticle(slug: String, param: UpdateArticleParam): Result<Article>
    fun deleteArticle(slug: String): Result<Article>
    fun favoriteArticle(user: User, slug: String): Result<Article>
    fun unfavoriteArticle(user: User, slug: String): Result<Article>
}
