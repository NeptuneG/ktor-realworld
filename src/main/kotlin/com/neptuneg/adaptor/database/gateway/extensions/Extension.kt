package com.neptuneg.adaptor.database.gateway.extensions

import org.jetbrains.exposed.sql.FieldSet
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("TooGenericExceptionCaught")
fun <R> runTxCatching(block: () -> R): Result<R> {
    return try {
        transaction { Result.success(block()) }
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

inline fun FieldSet.isExisting(where: SqlExpressionBuilder.() -> Op<Boolean>) =
    select(SqlExpressionBuilder.where()).count() != 0L
