package com.neptuneg.adaptor.database.gateway.repository

import com.neptuneg.adaptor.database.gateway.entity.ArticleEntity
import com.neptuneg.adaptor.database.gateway.extension.isExisting
import com.neptuneg.adaptor.database.gateway.extension.runTxCatching
import com.neptuneg.adaptor.database.gateway.table.*
import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entity.*
import com.neptuneg.domain.logic.ArticleRepository
import com.neptuneg.domain.logic.FollowingRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class ArticleRepositoryImpl(
    private val keycloakService: KeycloakService,
    private val followingRepository: FollowingRepository,
) : ArticleRepository {
    override fun findBySlug(slug: String, user: User?): Result<Article> {
        return runTxCatching {
            val article = ArticleEntity.find { ArticlesTable.slug.eq(slug) }.single()
            val tags = TagsTable.innerJoin(ArticleTagsTable)
                .slice(TagsTable.value)
                .select { ArticleTagsTable.articleId.eq(article.id) }
                .map { Tag(it[TagsTable.value]) }
            val isFavorited = user?.let {
                ArticleFavoritesTable.isExisting {
                    ArticleFavoritesTable.articleId.eq(article.id).and(ArticleFavoritesTable.favoriteeId.eq(it.id))
                }
            } ?: false
            val favoritesCount = ArticleFavoritesTable
                .select { ArticleFavoritesTable.articleId.eq(article.id) }
                .count()
            val author = keycloakService.findUser(article.authorId).getOrThrow()
            val isFollowing = user?.let { followingRepository.isExisting(user.id, author.id).getOrThrow() } ?: false

            article.toArticle(author, isFollowing, isFavorited, favoritesCount, tags)
        }
    }

    @Suppress("SpreadOperator")
    override fun fetchUserFeed(user: User, pagination: Pagination): Result<List<Article>> {
        return runTxCatching {
            val favoritedArticleIds = ArticleFavoritesTable
                .slice(ArticleFavoritesTable.articleId)
                .select { ArticleFavoritesTable.favoriteeId.eq(user.id) }
                .map { it[ArticleFavoritesTable.articleId] }

            ArticlesTable
                .join(
                    FollowingsTable,
                    JoinType.INNER,
                    additionalConstraint = { FollowingsTable.followeeId.eq(ArticlesTable.authorId) }
                )
                .innerJoin(ArticleTagsTable).innerJoin(TagsTable)
                .slice(tagsCol, *ArticlesTable.columns.toTypedArray())
                .select { FollowingsTable.followerId.eq(user.id) }
                .groupBy(*ArticlesTable.columns.toTypedArray())
                .limit(pagination.limit, pagination.offset)
                .map { result ->
                    val author = keycloakService.findUser(result[ArticlesTable.authorId]).getOrThrow()
                    val isFavorited = favoritedArticleIds.contains(result[ArticlesTable.id])
                    val favoritesCount = ArticleFavoritesTable.select {
                        ArticleFavoritesTable.articleId.eq(result[ArticlesTable.id])
                    }.count()

                    result.toArticle(author, true, isFavorited, favoritesCount)
                }
        }
    }

    override fun create(article: Article): Result<Article> {
        return runTxCatching {
            val articleId = ArticlesTable.insertAndGetId {
                it[slug] = article.slug
                it[title] = article.title
                it[description] = article.description
                it[body] = article.body
                it[authorId] = article.author.user.id
                it[createdAt] = article.createdAt
                it[updatedAt] = article.updatedAt
            }
            article.tags.map { tag ->
                val tagId = TagsTable.insertIgnoreAndGetId {
                    it[value] = tag.tag
                } ?: TagsTable.select { TagsTable.value.eq(tag.tag) }.map { it[TagsTable.id] }.single()
                ArticleTagsTable.insertIgnore {
                    it[ArticleTagsTable.articleId] = articleId
                    it[ArticleTagsTable.tagId] = tagId
                }
            }
            article
        }
    }

    override fun updateBySlug(slug: String, param: ArticleRepository.UpdateArticleParam): Result<Article> {
        return runTxCatching {
            ArticlesTable.update({ ArticlesTable.slug.eq(slug) }) {
                param.title?.let { title ->
                    it[ArticlesTable.title] = title
                }
                param.description?.let { description ->
                    it[ArticlesTable.description] = description
                }
                param.body?.let { body ->
                    it[ArticlesTable.body] = body
                }
            }
            findBySlug(slug).getOrThrow()
        }
    }

    override fun deleteBySlug(slug: String): Result<Article> {
        return runTxCatching {
            val article = findBySlug(slug).getOrThrow()
            ArticlesTable.deleteWhere { ArticlesTable.slug.eq(slug) }
            article
        }
    }

    @Suppress("LongMethod", "SpreadOperator")
    override fun search(param: ArticleRepository.SearchParam, user: User?): Result<List<Article>> {
        return runTxCatching {
            val favoritedArticleIds = user?.let {
                ArticleFavoritesTable
                    .slice(ArticleFavoritesTable.articleId)
                    .select { ArticleFavoritesTable.favoriteeId.eq(user.id) }
                    .map { it[ArticleFavoritesTable.articleId] }
            }
            val followeeIds: List<UUID> = user?.let {
                followingRepository.findFolloweeIds(it).getOrThrow()
            } ?: emptyList()
            val tables = ArticlesTable.innerJoin(ArticleTagsTable).innerJoin(TagsTable).let {
                param.favoritedUserName?.let { _ ->
                    it.innerJoin(ArticleFavoritesTable)
                } ?: it
            }

            tables.slice(tagsCol, *ArticlesTable.columns.toTypedArray())
                .selectAll().apply {
                    param.authorName?.let {
                        keycloakService.findUserByUsername(it).getOrNull()?.let { author ->
                            andWhere { ArticlesTable.authorId.eq(author.id) }
                        } ?: return@runTxCatching emptyList()
                    }
                    param.favoritedUserName?.let {
                        keycloakService.findUserByUsername(it).getOrNull()?.let { favoritee ->
                            andWhere { ArticleFavoritesTable.favoriteeId.eq(favoritee.id) }
                        } ?: return@runTxCatching emptyList()
                    }
                    param.tag?.let { tag ->
                        andWhere {
                            ArticlesTable.id.inSubQuery(
                                ArticleTagsTable
                                    .innerJoin(TagsTable)
                                    .slice(ArticleTagsTable.articleId)
                                    .select { TagsTable.value.eq(tag) }
                            )
                        }
                    }
                }
                .groupBy(*ArticlesTable.columns.toTypedArray())
                .limit(param.pagination.limit, param.pagination.offset)
                .map { result ->
                    val author = keycloakService.findUser(result[ArticlesTable.authorId]).getOrThrow()
                    val isFavorited = user?.let {
                        favoritedArticleIds!!.contains(result[ArticlesTable.id])
                    } ?: false
                    val favoritesCount = ArticleFavoritesTable.select {
                        ArticleFavoritesTable.articleId.eq(result[ArticlesTable.id])
                    }.count()
                    val isFollowing = user?.let { followeeIds.contains(author.id) } ?: false

                    result.toArticle(author, isFollowing, isFavorited, favoritesCount)
                }
        }
    }

    private val groupConcatSeparator = ","
    private val tagsCol = TagsTable.value.groupConcat(groupConcatSeparator).alias("tags")

    private fun ResultRow.toArticle(
        author: User,
        isFollowing: Boolean,
        isFavorited: Boolean,
        favoritesCount: Long,
    ) = Article(
        slug = this[ArticlesTable.slug],
        title = this[ArticlesTable.title],
        description = this[ArticlesTable.description],
        body = this[ArticlesTable.body],
        tags = this[tagsCol].split(groupConcatSeparator).map { Tag(it) },
        createdAt = this[ArticlesTable.createdAt],
        updatedAt = this[ArticlesTable.updatedAt],
        favorited = isFavorited,
        favoritesCount = favoritesCount,
        author = author.profile(isFollowing),
    )

    private fun ArticleEntity.toArticle(
        author: User,
        isFollowing: Boolean,
        isFavorited: Boolean,
        favoritesCount: Long,
        tags: List<Tag>
    ) = Article(
        slug = slug,
        title = title,
        description = description,
        body = body,
        tags = tags,
        createdAt = createdAt,
        updatedAt = updatedAt,
        favorited = isFavorited,
        favoritesCount = favoritesCount,
        author = author.profile(isFollowing),
    )
}
