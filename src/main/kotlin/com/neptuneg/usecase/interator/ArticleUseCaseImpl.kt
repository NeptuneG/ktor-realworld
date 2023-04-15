package com.neptuneg.usecase.interator

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Pagination
import com.neptuneg.domain.logics.ArticleRepository
import com.neptuneg.usecase.inputport.ArticleUseCase
import java.util.*

class ArticleUseCaseImpl(
    private val articleRepository: ArticleRepository,
) : ArticleUseCase {
    override fun create(authorId: UUID, param: ArticleUseCase.CreateParam): Result<Article> {
        return articleRepository.create(authorId, param.toRepositoryParam())
    }

    override fun find(slug: String): Result<Article> {
        return articleRepository.find(slug)
    }

    override fun listUserFeed(userId: UUID, pagination: Pagination): Result<List<Article>> {
        return articleRepository.listUserFeed(userId, pagination)
    }

    override fun search(param: ArticleUseCase.SearchParam): Result<List<Article>> {
        return articleRepository.search(param.toRepositoryParam())
    }

    override fun update(slug: String, param: ArticleUseCase.UpdateParam): Result<Article> {
        return articleRepository.find(slug).map { article ->
            param.title?.apply { article.title = this }
            param.description?.apply { article.description = this }
            param.body?.apply { article.body = this }
            articleRepository.update(article).getOrThrow()

            article
        }
    }

    override fun delete(slug: String): Result<Article> {
        return articleRepository.find(slug).onSuccess {
            articleRepository.delete(it).getOrThrow()
        }
    }

    override fun favoriteArticle(userId: UUID, slug: String): Result<Article> {
        return articleRepository.find(slug).map { article ->
            article.favoriterIds.add(userId)
            articleRepository.updateFavoriterIds(article).getOrThrow()

            article
        }
    }

    override fun unfavoriteArticle(userId: UUID, slug: String): Result<Article> {
        return articleRepository.find(slug).map { article ->
            article.favoriterIds.remove(userId)
            articleRepository.updateFavoriterIds(article).getOrThrow()

            article
        }
    }

    private fun ArticleUseCase.CreateParam.toRepositoryParam() =
        ArticleRepository.CreateParam(title, description, body, tags)

    private fun ArticleUseCase.SearchParam.toRepositoryParam() =
        ArticleRepository.SearchParam(tag, authorName, favoritedUserName, pagination)
}
