package com.neptuneg.adaptor.database.gateway.entities

import com.neptuneg.adaptor.database.gateway.tables.ArticlesTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ArticleEntity(id: EntityID<Int>) : IntEntity(id) {
    var authorId by ArticlesTable.authorId
    var slug by ArticlesTable.slug
    var title by ArticlesTable.title
    var description by ArticlesTable.description
    var body by ArticlesTable.body
    var createdAt by ArticlesTable.createdAt
    var updatedAt by ArticlesTable.updatedAt

    companion object : IntEntityClass<ArticleEntity>(ArticlesTable)
}
