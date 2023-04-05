package com.neptuneg

import com.neptuneg.adaptor.web.controller.sample
import com.neptuneg.adaptor.web.util.installAuthentication
import com.neptuneg.adaptor.web.util.installContentNegotiation
import com.neptuneg.infrastructure.serializer.Serializer
import com.neptuneg.usecase.inputport.Sample
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
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
