package com.neptuneg.domain.entity

import com.neptuneg.domain.entity.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class User(
    val username: String,
    val email: String,
    val password: String,
    val bio: String? = null,
    val image: String? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
)
