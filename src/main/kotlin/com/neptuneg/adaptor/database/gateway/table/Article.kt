package com.neptuneg.adaptor.database.gateway.table

import com.neptuneg.adaptor.database.gateway.util.datetimeWithTZ
import org.jetbrains.exposed.dao.id.IntIdTable

object Article: IntIdTable("articles") {
    val authorId = uuid(name = "author_id")
    val slug = varchar(name = "slug", length = 64).uniqueIndex()
    val title = varchar(name = "title", length = 64).uniqueIndex()
    val description = varchar(name = "description", length = 255)
    val body = text(name = "body")
    val createdAt = datetimeWithTZ(name = "created_at")
    val updatedAt = datetimeWithTZ(name = "updated_at")
}
