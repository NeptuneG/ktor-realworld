package com.neptuneg.adaptor.database

import com.neptuneg.adaptor.database.gateway.table.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.neptuneg.config.Database as DatabaseConfig

object DatabaseManager {
    fun connect(config: DatabaseConfig) {
        Database.connect(
            url = "jdbc:postgresql://${config.host}:${config.port}/${config.database}",
            driver = "org.postgresql.Driver",
            user = config.user,
            password = config.password,
        )
    }

    fun migrate() {
        transaction {
            SchemaUtils.create(UserTable)
        }
    }
}
