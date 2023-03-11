package com.neptuneg.adaptor.database.gateway.util

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.Table
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

// Ref: https://qiita.com/tumugin/items/be806107fa05f22d32ff
class DateTimeWithTimeZoneColumnType : ColumnType(), IDateColumnType {
    override val hasTimePart: Boolean = true

    override fun nonNullValueToString(value: Any): String {
        if (value !is Instant) {
            error("$value is not Instant type")
        }
        return "'${DateTimeFormatter.ISO_INSTANT.format(value)}'"
    }

    override fun valueFromDB(value: Any): Instant {
        return when (value) {
            is Instant -> value
            is LocalDateTime -> value.toInstant(ZoneOffset.UTC)
            is Timestamp -> value.toInstant()
            else -> error("$value is an instance of the unsupported class ${value.javaClass}")
        }
    }

    override fun notNullValueToDB(value: Any): Any {
        if (value !is Instant) {
            error("$value is not Instant type")
        }
        return value
    }

    override fun sqlType(): String = "TIMESTAMP WITH TIME ZONE"
}

fun Table.datetimeWithTZ(name: String): Column<Instant> = registerColumn(name, DateTimeWithTimeZoneColumnType())
