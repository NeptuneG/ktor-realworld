package com.neptuneg.domain.entity

import com.neptuneg.domain.entity.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class User(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val bio: String? = null,
    val image: String? = null,
    val token: String? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
)
