package com.neptuneg.adaptor.database.gateway.repository

import com.neptuneg.adaptor.database.gateway.extension.runTxCatching
import com.neptuneg.adaptor.database.gateway.table.ArticleFavoritesTable
import com.neptuneg.adaptor.database.gateway.table.ArticlesTable
import com.neptuneg.domain.entity.Article
import com.neptuneg.domain.entity.User
import com.neptuneg.domain.logic.ArticleRepository
import com.neptuneg.domain.logic.FavoriteRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select

class FavoriteRepositoryImpl(
    private val articleRepository: ArticleRepository
) : FavoriteRepository {
    override fun favoriteBySlug(user: User, slug: String): Result<Article> {
        return runTxCatching {
            val articleId = articleIdBySlug(slug)
            ArticleFavoritesTable.insertIgnore {
                it[ArticleFavoritesTable.articleId] = articleId
                it[favoriteeId] = user.id
            }
            articleRepository.findBySlug(slug, user).getOrThrow()
        }
    }

    override fun unfavoriteBySlug(user: User, slug: String): Result<Article> {
        return runTxCatching {
            val articleId = articleIdBySlug(slug)
            ArticleFavoritesTable.deleteWhere {
                ArticleFavoritesTable.articleId.eq(articleId)
                    .and(favoriteeId.eq(user.id))
            }
            articleRepository.findBySlug(slug, user).getOrThrow()
        }
    }

    private fun articleIdBySlug(slug: String) = ArticlesTable
        .slice(ArticlesTable.id)
        .select(ArticlesTable.slug.eq(slug))
        .map { it[ArticlesTable.id] }
        .single()
}
