package com.neptuneg.domain.entity

data class Profile (
    val username: String,
    val bio: String,
    val image: String,
    val following: Boolean = false,
)
