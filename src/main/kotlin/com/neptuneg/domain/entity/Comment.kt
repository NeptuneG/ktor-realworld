package com.neptuneg.domain.entity

import com.neptuneg.infrastructure.timezone.JST_ZONE_OFFSET
import java.time.OffsetDateTime

data class Comment(
    val id: Int? = null,
    val body: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val author: Profile,
) {
    constructor(
        body: String,
        author: Profile,
        createdAt: OffsetDateTime = OffsetDateTime.now(JST_ZONE_OFFSET)
    ) : this(
        body = body,
        author = author,
        createdAt = createdAt,
        updatedAt = createdAt,
    )
}
