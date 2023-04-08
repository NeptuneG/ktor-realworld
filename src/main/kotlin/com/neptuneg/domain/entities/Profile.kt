package com.neptuneg.domain.entities

data class Profile(
    val user: User,
    val following: Boolean = false,
)
