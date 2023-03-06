package com.neptuneg

import com.neptuneg.adaptor.web.Server.installGenerals
import com.neptuneg.adaptor.web.controller.sample
import com.neptuneg.adaptor.web.insertKoins
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            installGenerals()
            insertKoins()
            routing {
                sample()
            }
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("""{"message":"Good luck🐱"}""".trimIndent(), bodyAsText())
        }
    }
}
