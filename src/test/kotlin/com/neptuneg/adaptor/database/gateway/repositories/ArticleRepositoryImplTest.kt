package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.infrastructure.exceptions.UnexpectedException
import com.neptuneg.usecase.inputport.Sample
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.random.Random
import kotlin.random.nextUInt

class ArticleRepositoryImplTest : FunSpec({
    context("dummy") {
        val sample: Sample = mockk {
            every { foobar() } returns Sample.Message("test")
        }
        test("test") {
            sample.foobar().shouldBe(Sample.Message("test"))
        }
    }

    context("coroutine") {
        val random = Random(Instant.now().toEpochMilli())
        fun plusOne(a: Int): Int {
            if (a == 5) {
                throw UnexpectedException("is 5")
            }
            return a + 1
        }
        test("parallel map") {
            val list = (1..10).toList()
            val result = list.map {
                async {
                    val sleep = random.nextUInt() % 10u
                    delay((sleep * 1000u).toLong())
                    val result = plusOne(it)
                    println("slept $sleep second and get $result")
                    result
                }
            }.awaitAll()
            result.shouldContainExactly((2..11).toList())
        }
    }
})
