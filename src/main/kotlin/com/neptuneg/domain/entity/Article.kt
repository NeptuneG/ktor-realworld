package com.neptuneg.domain.entity

import java.time.Instant

data class Article(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tags: List<Tag>,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val favorited: Boolean = false,
    val favoritesCount: Int = 0,
    val author: Profile,
) {
    constructor(title: String, description: String, body: String, tags: List<Tag>, author: Profile): this(
        slug = generateSlug(title),
        title = title,
        description = description,
        body = body,
        tags = tags,
        author = author
    )

    private companion object {
        fun generateSlug(title: String): String {
            return title.replace(" ", "-").lowercase()
        }
    }
}
