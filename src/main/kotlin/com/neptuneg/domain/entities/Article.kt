package com.neptuneg.domain.entities

import com.neptuneg.infrastructure.timezone.JST_ZONE_OFFSET
import java.time.OffsetDateTime

data class Article(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tags: List<Tag>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val favorited: Boolean = false,
    val favoritesCount: Long = 0,
    val author: Profile,
) {
    constructor(
        title: String,
        description: String,
        body: String,
        tags: List<Tag>,
        author: Profile,
        createdAt: OffsetDateTime = OffsetDateTime.now(JST_ZONE_OFFSET)
    ) : this(
        slug = generateSlug(title),
        title = title,
        description = description,
        body = body,
        tags = tags,
        createdAt = createdAt,
        updatedAt = createdAt,
        author = author,
    )

    private companion object {
        fun generateSlug(title: String): String {
            return title.replace(" ", "-").lowercase()
        }
    }
}
