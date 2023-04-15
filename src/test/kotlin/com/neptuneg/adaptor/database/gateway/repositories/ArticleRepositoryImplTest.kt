package com.neptuneg.adaptor.database.gateway.repositories

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import java.time.Instant
import kotlin.random.Random
import kotlin.random.nextUInt

class ArticleRepositoryImplTest : FunSpec({
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
