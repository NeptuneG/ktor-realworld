package com.neptuneg.adaptor.web.util

import com.neptuneg.domain.entity.serializer.Serializer
import com.squareup.moshi.Moshi
import io.ktor.http.ContentType
import io.ktor.http.withCharsetIfNeeded
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
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
    ): OutgoingContent? {
        if (value == null) {
            return null
        }
        return TextContent(
            moshi.adapter(value.javaClass).toJson(value),
            contentType.withCharsetIfNeeded(charset)
        )
    }

    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        return withContext(Dispatchers.IO) {
            val body = content.toInputStream().reader(charset).buffered().use { it.readText() }
            if (body.isEmpty()) throw JsonConvertException("Empty Json data")
            moshi.adapter(typeInfo.type.java).fromJson(body)
        }
    }
}
