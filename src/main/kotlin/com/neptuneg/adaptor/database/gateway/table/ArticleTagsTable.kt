package com.neptuneg.adaptor.database.gateway.table

import org.jetbrains.exposed.dao.id.IntIdTable

object ArticleTagsTable: IntIdTable("article_tags") {
    val articleId = reference(name = "article_id", ArticlesTable)
    val tagId = reference(name = "tag_id", TagsTable)

    init {
        uniqueIndex(articleId, tagId)
    }
}
