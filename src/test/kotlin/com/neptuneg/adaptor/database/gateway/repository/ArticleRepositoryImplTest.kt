package com.neptuneg.adaptor.database.gateway.repository

import com.neptuneg.usecase.inputport.Sample
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ArticleRepositoryImplTest : FunSpec({
    context("dummly") {
        val sample : Sample = mockk {
            every { foobar() } returns Sample.Message("test")
        }
        test("test") {
            sample.foobar().shouldBe(Sample.Message("test"))
        }
    }
})
