package com.neptuneg.adaptor.database

import com.neptuneg.adaptor.database.gateway.table.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseManager {
    fun connect(
        host: String = "0.0.0.0",
        port: Int = 5432,
        database: String,
        user: String,
        password: String
    ) {
        Database.connect(
            url = "jdbc:postgresql://$host:$port/$database",
            driver = "org.postgresql.Driver",
            user = user,
            password = password,
        )
    }

    fun migrate() {
        transaction {
            SchemaUtils.create(UserTable)
        }
    }
}
