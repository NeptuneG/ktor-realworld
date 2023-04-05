package com.neptuneg.adaptor.database

import com.neptuneg.Database
import com.neptuneg.infrastructure.config.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database as ExposedDatabase

class ExposedPostgres(private val config: DatabaseConfig) : Database {
    override fun connect() = ExposedDatabase.connect(dataSource).let { }

    private val dataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = config.jdbcUrl
            username = config.username
            password = config.password
            driverClassName = config.driverClassName
        }
    )
}
