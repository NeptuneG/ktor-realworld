package com.neptuneg

import com.neptuneg.adaptor.web.controllers.sample
import com.neptuneg.adaptor.web.utils.installAuthentication
import com.neptuneg.adaptor.web.utils.installContentNegotiation
import com.neptuneg.infrastructure.serializer.Serializer
import com.neptuneg.usecase.inputport.Sample
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.koin.java.KoinJavaComponent
import kotlin.test.assertEquals

class ApplicationTest : FunSpec({
    fun Sample.Message.toJson(): String = Serializer.moshi.adapter(javaClass).toJson(this)

    context("when accessing the root") {
        test("returns welcome message") {
            testApplication {
                insertKoins()
                application {
                    installContentNegotiation()
                    installAuthentication(KoinJavaComponent.getKoin().get())
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
    }
})
