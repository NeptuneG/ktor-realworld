package com.neptuneg.adaptor.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import com.neptuneg.config.DatabaseConfig

fun buildHikariDataSource(config: DatabaseConfig): DataSource = HikariDataSource(
    HikariConfig().apply {
        jdbcUrl = config.jdbcUrl
        username = config.username
        password = config.password
        driverClassName = config.driverClassName
    }
)
