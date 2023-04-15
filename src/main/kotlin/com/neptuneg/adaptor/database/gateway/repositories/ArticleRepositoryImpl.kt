package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.adaptor.database.gateway.entities.ArticleEntity
import com.neptuneg.adaptor.database.gateway.extensions.runTxCatching
import com.neptuneg.adaptor.database.gateway.tables.ArticleFavoritesTable
import com.neptuneg.adaptor.database.gateway.tables.ArticleTagsTable
import com.neptuneg.adaptor.database.gateway.tables.ArticlesTable
import com.neptuneg.adaptor.database.gateway.tables.FollowingsTable
import com.neptuneg.adaptor.database.gateway.tables.TagsTable
import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Pagination
import com.neptuneg.domain.entities.Tag
import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.ArticleRepository
import com.neptuneg.domain.logics.UserRepository
import com.neptuneg.infrastructure.timezone.now
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.groupConcat
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.util.*

@Suppress("TooManyFunctions")
class ArticleRepositoryImpl(
    private val userRepository: UserRepository,
) : ArticleRepository {
    override fun find(slug: String): Result<Article> {
        return runTxCatching {
            val article = ArticleEntity.find { ArticlesTable.slug.eq(slug) }.single()
            val tags = TagsTable.innerJoin(ArticleTagsTable)
                .slice(TagsTable.value)
                .select { ArticleTagsTable.articleId.eq(article.id) }
                .map { Tag(it[TagsTable.value]) }
                .toMutableList()
            val author = userRepository.find(article.authorId).getOrThrow()

            Article(
                slug = article.slug,
                title = article.title,
                description = article.description,
                body = article.body,
                author = author,
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
                favoriterIds = articleFavoriterIds(article.id),
                tags = tags,
            )
        }
    }

    @Suppress("SpreadOperator")
    override fun listUserFeed(userId: UUID, pagination: Pagination): Result<List<Article>> {
        return runTxCatching {
            val results = ArticlesTable
                .join(
                    FollowingsTable,
                    JoinType.INNER,
                    additionalConstraint = { FollowingsTable.followingUserId.eq(ArticlesTable.authorId) }
                )
                .innerJoin(ArticleTagsTable).innerJoin(TagsTable)
                .slice(tagsCol, *ArticlesTable.columns.toTypedArray())
                .select { FollowingsTable.followerId.eq(userId) }
                .groupBy(*ArticlesTable.columns.toTypedArray())
                .orderBy(ArticlesTable.updatedAt, SortOrder.DESC)
                .limit(pagination.limit, pagination.offset)

            runBlocking {
                results.map { result ->
                    async {
                        val author = userRepository.find(result[ArticlesTable.authorId]).getOrThrow()
                        val favoriterIds = articleFavoriterIds(result[ArticlesTable.id])

                        result.toArticle(author, favoriterIds)
                    }
                }.awaitAll()
            }
        }
    }

    override fun create(authorId: UUID, param: ArticleRepository.CreateParam): Result<Article> {
        return runTxCatching {
            val now = now()
            val slug = param.title.slug()
            val articleId = ArticlesTable.insertAndGetId {
                it[ArticlesTable.slug] = slug
                it[title] = param.title
                it[description] = param.description
                it[body] = param.body
                it[ArticlesTable.authorId] = authorId
                it[createdAt] = now
                it[updatedAt] = now
            }
            runBlocking {
                param.tags.map { tag ->
                    async {
                        val tagId = TagsTable.insertIgnoreAndGetId { it[value] = tag }
                            ?: TagsTable.select { TagsTable.value.eq(tag) }.map { it[TagsTable.id] }.single()
                        ArticleTagsTable.insertIgnore {
                            it[ArticleTagsTable.articleId] = articleId
                            it[ArticleTagsTable.tagId] = tagId
                        }
                    }
                }.awaitAll()
            }
            val author = userRepository.find(authorId).getOrThrow()
            Article(
                slug = slug,
                title = param.title,
                description = param.description,
                body = param.body,
                author = author,
                createdAt = now,
                updatedAt = now,
                favoriterIds = mutableListOf(),
                tags = param.tags.map { Tag(it) }.toMutableList(),
            )
        }
    }

    override fun update(article: Article): Result<Unit> {
        return runTxCatching {
            ArticlesTable.update({ ArticlesTable.slug.eq(article.slug) }) {
                it[title] = title
                it[description] = description
                it[body] = body
            }
        }
    }

    override fun updateFavoriterIds(article: Article): Result<Unit> {
        return runTxCatching {
            val articleId = ArticleEntity.find { ArticlesTable.slug.eq(article.slug) }.single().id
            ArticleFavoritesTable.deleteWhere { ArticleFavoritesTable.articleId.eq(articleId) }
            ArticleFavoritesTable.batchInsert(article.favoriterIds) {
                this[ArticleFavoritesTable.articleId] = articleId
                this[ArticleFavoritesTable.favoriterId] = it
            }
        }
    }

    override fun delete(article: Article): Result<Unit> {
        return runTxCatching {
            ArticlesTable.deleteWhere { slug.eq(article.slug) }
        }
    }

    @Suppress("LongMethod", "SpreadOperator", "CyclomaticComplexMethod")
    override fun search(param: ArticleRepository.SearchParam): Result<List<Article>> {
        return runTxCatching {
            val tables = ArticlesTable.innerJoin(ArticleTagsTable).innerJoin(TagsTable).let {
                param.favoritedUserName?.let { _ ->
                    it.innerJoin(ArticleFavoritesTable)
                } ?: it
            }

            val results = tables.slice(tagsCol, *ArticlesTable.columns.toTypedArray())
                .selectAll().apply {
                    param.authorName?.let {
                        userRepository.find(it).getOrNull()?.let { author ->
                            andWhere { ArticlesTable.authorId.eq(author.id) }
                        } ?: return@runTxCatching emptyList()
                    }
                    param.favoritedUserName?.let {
                        userRepository.find(it).getOrNull()?.let { favoritee ->
                            andWhere { ArticleFavoritesTable.favoriterId.eq(favoritee.id) }
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
                .orderBy(ArticlesTable.updatedAt, SortOrder.DESC)
                .limit(param.pagination.limit, param.pagination.offset)

            runBlocking {
                results.map { result ->
                    async {
                        val author = userRepository.find(result[ArticlesTable.authorId]).getOrThrow()
                        val favoriterIds = articleFavoriterIds(result[ArticlesTable.id])

                        result.toArticle(author, favoriterIds)
                    }
                }.awaitAll()
            }
        }
    }

    private val groupConcatSeparator = ","
    private val tagsCol = TagsTable.value.groupConcat(groupConcatSeparator).alias("tags")

    private fun String.slug() = replace(" ", "-").lowercase()

    private fun ResultRow.toArticle(
        author: User,
        favoriterIds: MutableList<UUID>,
    ) = Article(
        slug = this[ArticlesTable.slug],
        title = this[ArticlesTable.title],
        description = this[ArticlesTable.description],
        body = this[ArticlesTable.body],
        author = author,
        createdAt = this[ArticlesTable.createdAt],
        updatedAt = this[ArticlesTable.updatedAt],
        favoriterIds = favoriterIds,
        tags = this[tagsCol].split(groupConcatSeparator).map { Tag(it) }.toMutableList(),
    )

    private fun articleFavoriterIds(articleId: EntityID<Int>): MutableList<UUID> =
        ArticleFavoritesTable.innerJoin(ArticlesTable)
            .slice(ArticleFavoritesTable.favoriterId)
            .select { ArticleFavoritesTable.articleId.eq(articleId) }
            .map { it[ArticleFavoritesTable.favoriterId] }
            .toMutableList()
}
