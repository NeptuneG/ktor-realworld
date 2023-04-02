package com.neptuneg.domain.entity

data class Profile (
    val user: User,
    val following: Boolean = false,
)
