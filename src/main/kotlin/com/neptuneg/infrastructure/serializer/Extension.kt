package com.neptuneg.infrastructure.serializer

fun Any.toJson(): String = Serializer.moshi.adapter(javaClass).toJson(this)
