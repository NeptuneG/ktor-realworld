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

    constructor(id: Int, comment: Comment): this(
        id = id,
        body = comment.body,
        createdAt = comment.createdAt,
        updatedAt = comment.updatedAt,
        author = comment.author
    )

    fun withId(id: Int) = Comment(id, this)
}
