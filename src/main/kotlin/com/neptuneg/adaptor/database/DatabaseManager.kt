package com.neptuneg.adaptor.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import com.neptuneg.config.Database as DatabaseConfig

object DatabaseManager {
    fun connect(config: DatabaseConfig) = Database.connect(
        HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = config.jdbcUrl
                username = config.username
                password = config.password
                driverClassName = config.driverClassName
            }
        )
    )
}
