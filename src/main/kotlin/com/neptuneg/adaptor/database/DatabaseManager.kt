package com.neptuneg.adaptor.database

import com.neptuneg.adaptor.database.gateway.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.neptuneg.config.Database as DatabaseConfig

class DatabaseManager(private val config: DatabaseConfig) {
    fun connect() {
        val dataSource = hikariDataSource
        Database.connect(dataSource)
    }

    fun migrate() {
        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    private val hikariDataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = config.jdbcUrl
            username = config.username
            password = config.password
            driverClassName = config.driverClassName
        }
    )
}
