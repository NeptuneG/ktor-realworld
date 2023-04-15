package com.neptuneg.domain.entities

import java.time.OffsetDateTime

data class Comment(
    val id: Int,
    var body: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val author: User,
)
