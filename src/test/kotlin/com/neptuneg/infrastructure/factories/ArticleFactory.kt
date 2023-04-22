package com.neptuneg.infrastructure.factories

import com.neptuneg.adaptor.database.gateway.tables.ArticleFavoritesTable
import com.neptuneg.adaptor.database.gateway.tables.ArticleTagsTable
import com.neptuneg.adaptor.database.gateway.tables.ArticlesTable
import com.neptuneg.adaptor.database.gateway.tables.TagsTable
import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.Tag
import com.neptuneg.infrastructure.timezone.now
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object ArticleFactory {
    @Suppress("LongParameterList")
    fun create(
        title: String = faker.book.unique.title(),
        description: String = faker.lorem.words(),
        body: String = faker.lorem.words(),
        authorId: UUID = UUID.randomUUID(),
        favoriterIds: List<UUID> = listOf(UUID.randomUUID()),
        tags: List<String> = listOf(),
    ): Article {
        return transaction {
            val now = now()
            val slug = title.replace(" ", "-").lowercase()
            val articleId = ArticlesTable.insertAndGetId {
                it[ArticlesTable.title] = title
                it[ArticlesTable.slug] = slug
                it[ArticlesTable.description] = description
                it[ArticlesTable.body] = body
                it[ArticlesTable.authorId] = authorId
                it[createdAt] = now
                it[updatedAt] = now
            }
            tags.forEach { tag ->
                val tagId = TagsTable.insertIgnoreAndGetId { it[value] = tag }
                    ?: TagsTable.select { TagsTable.value.eq(tag) }.map { it[TagsTable.id] }.single()
                ArticleTagsTable.insertIgnore {
                    it[ArticleTagsTable.articleId] = articleId
                    it[ArticleTagsTable.tagId] = tagId
                }
            }
            ArticleFavoritesTable.batchInsert(favoriterIds) {
                this[ArticleFavoritesTable.articleId] = articleId
                this[ArticleFavoritesTable.favoriterId] = it
            }
            Article(
                slug = slug,
                title = title,
                description = description,
                body = body,
                author = UserFactory.build(authorId),
                createdAt = now,
                updatedAt = now,
                favoriterIds = favoriterIds.toMutableList(),
                tags = tags.map { Tag(it) }.toMutableList()
            )
        }
    }
}
