package com.neptuneg.infrastructure

import com.neptuneg.adaptor.database.gateway.tables.ArticleFavoritesTable
import com.neptuneg.adaptor.database.gateway.tables.ArticleTagsTable
import com.neptuneg.adaptor.database.gateway.tables.ArticlesTable
import com.neptuneg.adaptor.database.gateway.tables.CommentsTable
import com.neptuneg.adaptor.database.gateway.tables.FollowingsTable
import com.neptuneg.adaptor.database.gateway.tables.TagsTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabase {
    private val dataSource = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://0.0.0.0:5432/realworld_test"
            username = "admin"
            password = "password"
            driverClassName = "org.postgresql.Driver"
        }
    )

    private val tables = listOf(
        ArticleFavoritesTable,
        ArticleTagsTable,
        TagsTable,
        CommentsTable,
        ArticlesTable,
        FollowingsTable,
    )

    init {
        Flyway.configure()
            .dataSource(dataSource)
            .locations("filesystem:./flyway/sql")
            .load()
            .migrate()
    }

    fun connect() = Database.connect(dataSource)

    fun clear() = transaction {
        tables.forEach { it.deleteAll() }
    }
}
