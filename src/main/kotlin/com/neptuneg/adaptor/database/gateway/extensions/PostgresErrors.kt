package com.neptuneg.adaptor.database.gateway.extensions

import org.jetbrains.exposed.exceptions.ExposedSQLException

internal enum class PostgresErrors(val code: String) {
    // https://www.postgresql.jp/document/13/html/errcodes-appendix.html
    ForeignKeyViolation("23503"),
    UniqueViolation("23505"),
    Other("Other");

    companion object {
        fun by(e: ExposedSQLException) = PostgresErrors.values().find { it.code == e.sqlState } ?: Other
    }
}
