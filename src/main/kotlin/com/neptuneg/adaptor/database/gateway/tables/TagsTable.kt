package com.neptuneg.adaptor.database.gateway.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object TagsTable : IntIdTable("tags") {
    val value = varchar(name = "value", length = 16).uniqueIndex()
}
