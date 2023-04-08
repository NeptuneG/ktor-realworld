package com.neptuneg.adaptor.database.gateway.repositories

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
        test("parallel map") {
            fun plusOne(a: Int) = (a + 1)
            val random = Random(Instant.now().toEpochMilli())
            (1..10)
                .toList()
                .map {
                    async {
                        val sleep = (random.nextUInt() % 10u).apply {
                            delay((this * 1000u).toLong())
                        }
                        plusOne(it).apply {
                            println("slept $sleep second and get $this")
                        }
                    }
                }
                .awaitAll()
                .shouldContainExactly((2..11).toList())
        }
    }
})
