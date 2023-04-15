package com.neptuneg.domain.entities

import java.util.UUID

data class User(
    val id: UUID,
    var username: String,
    var email: String,
    var bio: String = "",
    var image: String = "",
    val token: String? = null,
    val followerIds: MutableList<UUID>,
    val followingUserIds: MutableList<UUID>,
)
