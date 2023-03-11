package com.neptuneg.adaptor.database.gateway.table

import com.neptuneg.adaptor.database.gateway.util.datetimeWithTZ
import org.jetbrains.exposed.sql.Table

@Suppress("MagicNumber")
object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 255).uniqueIndex("users_unique_username_index")
    val email = varchar("email", 255).uniqueIndex("users_unique_email_index")
    val password = varchar("password", 255)
    val bio = varchar("bio", 255).nullable()
    val image = varchar("image", 255).nullable()
    val createdAt = datetimeWithTZ("created_at")
    val updatedAt = datetimeWithTZ("updated_at")
}
