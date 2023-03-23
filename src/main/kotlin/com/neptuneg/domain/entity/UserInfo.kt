package com.neptuneg.domain.entity

import com.squareup.moshi.Json

data class UserInfo(
    val sub: String,
    val email: String,
    @Json(name = "email_verified")
    val emailVerified: Boolean,
    @Json(name = "preferred_username")
    val preferredUsername: String,
    val bio: String?,
    val image: String?,
)
