package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.domain.logics.ArticleRepository
import com.neptuneg.infrastructure.RepositorySpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.mockk.mockk

class ArticleRepositoryImplTest : RepositorySpec({
    context("search") {
        context("when searching by tag") {
            val testee = ArticleRepositoryImpl(mockk())

            context("when no article is with the specified tag") {
                test("returns empty") {
                    val result = testee.search(ArticleRepository.SearchParam(tag = "foobar"))
                    result.isSuccess.shouldBeTrue()
                    result.onSuccess {
                        it.shouldBeEmpty()
                    }
                }
            }
        }
    }
})
