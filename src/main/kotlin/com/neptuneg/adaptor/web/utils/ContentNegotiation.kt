package com.neptuneg.adaptor.web.utils

import com.neptuneg.infrastructure.serializer.Serializer
import com.squareup.moshi.Moshi
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.http.withCharsetIfNeeded
import io.ktor.serialization.Configuration
import io.ktor.serialization.ContentConverter
import io.ktor.serialization.JsonConvertException
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Application.installContentNegotiation() {
    install(ContentNegotiation) {
        moshi()
    }
}

fun Configuration.moshi(
    contentType: ContentType = ContentType.Application.Json,
    block: Moshi.Builder.() -> Unit = {}
) {
    val builder = Serializer.moshiBuilder.apply(block)
    val converter = MoshiConverter(builder.build())
    register(contentType, converter)
}

class MoshiConverter(private val moshi: Moshi) : ContentConverter {
    override suspend fun serializeNullable(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?
    ): OutgoingContent? = value?.let {
        TextContent(
            moshi.adapter(it.javaClass).toJson(it),
            contentType.withCharsetIfNeeded(charset)
        )
    }

    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        return withContext(Dispatchers.IO) {
            content
                .toInputStream()
                .reader(charset)
                .buffered()
                .use { it.readText() }
                .apply { if (isEmpty()) throw JsonConvertException("can't be empty") }
                .let { moshi.adapter(typeInfo.type.java).fromJson(it) }
        }
    }
}
