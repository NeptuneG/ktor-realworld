package com.neptuneg.domain.entity.serializer

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ByteArrayAdapter {
    @ToJson
    fun toJson(data: ByteArray): String = String(data)

    @FromJson
    fun fromJson(data: String): ByteArray = data.toByteArray()
}
