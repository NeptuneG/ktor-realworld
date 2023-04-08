package com.neptuneg.usecase.interator

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Pagination
import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.ArticleRepository
import com.neptuneg.domain.logics.FavoriteRepository
import com.neptuneg.usecase.inputport.ArticleUseCase

class ArticleUseCaseImpl(
    private val articleRepository: ArticleRepository,
    private val favoriteRepository: FavoriteRepository,
) : ArticleUseCase {
    override fun createArticle(article: Article): Result<Article> {
        return articleRepository.create(article)
    }

    override fun findArticle(user: User?, slug: String): Result<Article> {
        return articleRepository.findBySlug(slug, user)
    }

    override fun fetchUserFeed(user: User, pagination: Pagination): Result<List<Article>> {
        return articleRepository.fetchUserFeed(user, pagination)
    }

    override fun searchArticles(user: User?, param: ArticleUseCase.SearchParam): Result<List<Article>> {
        return articleRepository.search(param.toRepositoryParam(), user)
    }

    override fun updateArticle(slug: String, param: ArticleUseCase.UpdateArticleParam): Result<Article> {
        return articleRepository.updateBySlug(slug, param.toRepositoryParam())
    }

    override fun deleteArticle(slug: String): Result<Article> {
        return articleRepository.deleteBySlug(slug)
    }

    override fun favoriteArticle(user: User, slug: String): Result<Article> {
        return favoriteRepository.favoriteBySlug(user, slug)
    }

    override fun unfavoriteArticle(user: User, slug: String): Result<Article> {
        return favoriteRepository.unfavoriteBySlug(user, slug)
    }

    private fun ArticleUseCase.SearchParam.toRepositoryParam() = ArticleRepository.SearchParam(
        tag = tag,
        authorName = authorName,
        favoritedUserName = favoritedUserName,
        pagination = pagination,
    )

    private fun ArticleUseCase.UpdateArticleParam.toRepositoryParam() = ArticleRepository.UpdateArticleParam(
        title = title,
        description = description,
        body = body,
    )
}
