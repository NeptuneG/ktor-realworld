package com.neptuneg.adaptor.database.gateway.table

import org.jetbrains.exposed.dao.id.IntIdTable

object ArticleTag: IntIdTable("article_tags") {
    val articleId = reference(name = "article_id", Article)
    val tagId = reference(name = "tag_id", Tag)

    init {
        uniqueIndex(articleId, tagId)
    }
}
