package com.neptuneg.adaptor.database.gateway.utils

import com.neptuneg.infrastructure.timezone.JST_ZONE_OFFSET
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.Table
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

// Ref: https://qiita.com/tumugin/items/be806107fa05f22d32ff
class DateTimeWithTimeZoneColumnType : ColumnType(), IDateColumnType {
    override val hasTimePart: Boolean = true

    override fun nonNullValueToString(value: Any): String {
        if (value !is OffsetDateTime) {
            error("$value is not OffsetDateTime type")
        }
        return "'${DateTimeFormatter.ISO_INSTANT.format(value)}'"
    }

    override fun valueFromDB(value: Any): OffsetDateTime {
        return when (value) {
            is OffsetDateTime -> value
            is Instant -> value.atOffset(JST_ZONE_OFFSET)
            is LocalDateTime -> value.atOffset(JST_ZONE_OFFSET)
            is Timestamp -> value.toInstant().atOffset(JST_ZONE_OFFSET)
            else -> error("$value is an instance of the unsupported class ${value.javaClass}")
        }
    }

    override fun notNullValueToDB(value: Any): Any {
        if (value !is OffsetDateTime) {
            error("$value is not OffsetDateTime type")
        }
        return value
    }

    override fun sqlType(): String = "TIMESTAMP WITH TIME ZONE"
}

fun Table.datetimeWithTZ(name: String): Column<OffsetDateTime> = registerColumn(name, DateTimeWithTimeZoneColumnType())
