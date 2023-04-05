package com.neptuneg.adaptor.database.gateway.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object ArticleFavoritesTable : IntIdTable("article_favorites") {
    val articleId = reference(name = "article_id", ArticlesTable)
    val favoriteeId = uuid(name = "favoritee_id")

    init {
        uniqueIndex(articleId, favoriteeId)
    }
}
