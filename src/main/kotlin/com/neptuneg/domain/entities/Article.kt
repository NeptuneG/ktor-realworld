package com.neptuneg.domain.entities

import java.time.OffsetDateTime
import java.util.*

data class Article(
    var slug: String,
    var title: String,
    var description: String,
    var body: String,
    val author: User,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val favoriterIds: MutableList<UUID>,
    val tags: MutableList<Tag>,
)
