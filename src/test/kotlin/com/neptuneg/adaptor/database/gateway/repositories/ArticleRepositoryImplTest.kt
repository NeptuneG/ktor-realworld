package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.logics.ArticleRepository
import com.neptuneg.domain.logics.UserRepository
import com.neptuneg.infrastructure.RepositorySpec
import com.neptuneg.infrastructure.factories.ArticleFactory
import com.neptuneg.infrastructure.factories.faker
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk

class ArticleRepositoryImplTest : RepositorySpec({
    context("search") {
        context("when searching by tag") {
            context("when there is no article with the specified tag") {
                val testee = ArticleRepositoryImpl(mockk())
                test("returns empty") {
                    val result = testee.search(ArticleRepository.SearchParam(tag = "foobar"))
                    result.shouldBeSuccess(emptyList())
                }
            }

            context("when there are articles with the specified tag") {
                val tag = faker.artist.unique.names()
                val anotherTag = faker.artist.unique.names()
                val articlesWithTheTag = mutableListOf<Article>().apply {
                    repeat(3) { add(ArticleFactory.create(tags = listOf(tag))) }
                }
                ArticleFactory.create(tags = listOf(anotherTag))

                val testee = ArticleRepositoryImpl(
                    mockk<UserRepository>().apply {
                        articlesWithTheTag.forEach {
                            every { find(it.author.id) } returns Result.success(it.author)
                        }
                    }
                )

                test("returns the articles with the specified tag") {
                    val expected = articlesWithTheTag.sortedByDescending { article -> article.updatedAt }
                    val result = testee.search(ArticleRepository.SearchParam(tag = tag))
                    result.shouldBeSuccess(expected)
                }
            }
        }
    }
})
