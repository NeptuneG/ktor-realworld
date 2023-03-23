package com.neptuneg.domain.entity

data class User(
    val username: String,
    val email: String,
    val bio: String? = null,
    val image: String? = null,
)
