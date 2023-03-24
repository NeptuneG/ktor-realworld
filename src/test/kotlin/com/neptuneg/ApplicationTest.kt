package com.neptuneg

import com.neptuneg.adaptor.web.controller.sample
import com.neptuneg.adaptor.web.util.installAuthentication
import com.neptuneg.adaptor.web.util.installContentNegotiation
import com.neptuneg.domain.entity.serializer.toJson
import com.neptuneg.usecase.inputport.Sample
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication

import org.junit.Test
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        insertKoins()
        application {
            installContentNegotiation()
            installAuthentication(getKoin().get())
            routing {
                sample()
            }
        }
        client.get("/").apply {
            val expectedBody = Sample.Message("Good luck🐱").toJson()
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(expectedBody, bodyAsText())
        }
    }
}
