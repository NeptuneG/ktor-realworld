package com.neptuneg.adaptor.database.gateway.tables

import com.neptuneg.adaptor.database.gateway.utils.datetimeWithTZ
import org.jetbrains.exposed.dao.id.IntIdTable

object CommentsTable : IntIdTable("comments") {
    val authorId = uuid(name = "author_id")
    val articleId = reference(name = "article_id", ArticlesTable)
    val body = text(name = "body")
    val createdAt = datetimeWithTZ(name = "created_at")
    val updatedAt = datetimeWithTZ(name = "updated_at")
}
