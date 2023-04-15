package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.adaptor.database.gateway.extensions.runTxCatching
import com.neptuneg.adaptor.database.gateway.tables.ArticlesTable
import com.neptuneg.adaptor.database.gateway.tables.CommentsTable
import com.neptuneg.domain.entities.Comment
import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.CommentRepository
import com.neptuneg.domain.logics.UserRepository
import com.neptuneg.infrastructure.timezone.now
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import java.util.*

class CommentRepositoryImpl(
    private val userRepository: UserRepository
) : CommentRepository {
    override fun find(id: Int): Result<Comment> {
        return runTxCatching {
            CommentsTable
                .select { CommentsTable.id.eq(id) }
                .map {
                    val author = userRepository.find(it[CommentsTable.authorId]).getOrThrow()
                    it.toComment(author)
                }
                .single()
        }
    }

    override fun list(articleSlug: String): Result<List<Comment>> {
        return runTxCatching {
            val result = CommentsTable.innerJoin(ArticlesTable)
                .slice(CommentsTable.columns)
                .select { ArticlesTable.slug.eq(articleSlug) }

            runBlocking {
                result.map {
                    async {
                        val author = userRepository.find(it[CommentsTable.authorId]).getOrThrow()
                        it.toComment(author)
                    }
                }.awaitAll()
            }
        }
    }

    override fun create(authorId: UUID, articleSlug: String, body: String): Result<Comment> {
        return runTxCatching {
            val now = now()
            val articleId = ArticlesTable
                .slice(ArticlesTable.id)
                .select(ArticlesTable.slug.eq(articleSlug))
                .map { it[ArticlesTable.id] }
                .single()
            val author = userRepository.find(authorId).getOrThrow()
            CommentsTable.insertAndGetId {
                it[CommentsTable.articleId] = articleId
                it[CommentsTable.authorId] = authorId
                it[CommentsTable.body] = body
                it[createdAt] = now
                it[updatedAt] = now
            }.let {
                Comment(it.value, body, now, now, author)
            }
        }
    }

    override fun delete(comment: Comment): Result<Unit> {
        return runTxCatching {
            CommentsTable.deleteWhere { CommentsTable.id.eq(comment.id) }
        }
    }

    private fun ResultRow.toComment(author: User) = Comment(
        id = this[CommentsTable.id].value,
        body = this[CommentsTable.body],
        createdAt = this[CommentsTable.createdAt],
        updatedAt = this[CommentsTable.updatedAt],
        author = author
    )
}
