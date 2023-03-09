package com.neptuneg.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val email: String,
    val password: String,
    val bio: String?,
    val image: String?,
)
