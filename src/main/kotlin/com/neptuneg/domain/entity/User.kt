package com.neptuneg.domain.entity

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val bio: String = "",
    val image: String = "",
) {
    fun profile(isFollowing: Boolean = false) = Profile(this, isFollowing)
}
