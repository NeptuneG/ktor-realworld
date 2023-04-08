package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.adaptor.database.gateway.extensions.runTxCatching
import com.neptuneg.adaptor.database.gateway.tables.ArticlesTable
import com.neptuneg.adaptor.database.gateway.tables.CommentsTable
import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entities.Comment
import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.CommentRepository
import com.neptuneg.domain.logics.FollowingRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select

class CommentRepositoryImpl(
    private val followingRepository: FollowingRepository,
    private val keycloakService: KeycloakService,
) : CommentRepository {
    override fun getArticleComments(articleSlug: String, user: User?): Result<List<Comment>> {
        return runTxCatching {
            val followeeIds = user?.let {
                followingRepository.findFolloweeIds(user).getOrThrow()
            } ?: emptyList()
            val result = CommentsTable.innerJoin(ArticlesTable)
                .slice(CommentsTable.columns)
                .select { ArticlesTable.slug.eq(articleSlug) }

            runBlocking {
                result.map {
                    async {
                        val author = keycloakService.findUser(it[CommentsTable.authorId]).getOrThrow()
                        Comment(
                            id = it[CommentsTable.id].value,
                            body = it[CommentsTable.body],
                            createdAt = it[CommentsTable.createdAt],
                            updatedAt = it[CommentsTable.updatedAt],
                            author = author.profile(followeeIds.contains(author.id))
                        )
                    }
                }.awaitAll()
            }
        }
    }

    override fun createComment(articleSlug: String, comment: Comment): Result<Comment> {
        return runTxCatching {
            val articleId = ArticlesTable
                .slice(ArticlesTable.id)
                .select(ArticlesTable.slug.eq(articleSlug))
                .map { it[ArticlesTable.id] }
                .single()
            val id = CommentsTable.insertAndGetId {
                it[CommentsTable.articleId] = articleId
                it[authorId] = comment.author.user.id
                it[body] = comment.body
                it[createdAt] = comment.createdAt
                it[updatedAt] = comment.updatedAt
            }
            comment.withId(id.value)
        }
    }

    override fun deleteComment(commentId: Int): Result<Comment> {
        return runTxCatching {
            CommentsTable
                .select { CommentsTable.id.eq(commentId) }
                .map {
                    val author = keycloakService.findUser(it[CommentsTable.authorId]).getOrThrow()
                    Comment(
                        id = it[CommentsTable.id].value,
                        body = it[CommentsTable.body],
                        createdAt = it[CommentsTable.createdAt],
                        updatedAt = it[CommentsTable.updatedAt],
                        author = author.profile()
                    )
                }
                .single()
                .apply {
                    CommentsTable.deleteWhere { CommentsTable.id.eq(commentId) }
                }
        }
    }
}
