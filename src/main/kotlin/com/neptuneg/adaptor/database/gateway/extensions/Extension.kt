package com.neptuneg.adaptor.database.gateway.extensions

import com.neptuneg.infrastructure.exceptions.ConflictException
import com.neptuneg.infrastructure.exceptions.NotFoundException
import com.neptuneg.infrastructure.exceptions.UnexpectedException
import com.neptuneg.infrastructure.exceptions.UnprocessableEntityException
import org.jetbrains.exposed.exceptions.ExposedSQLException
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
        when (e) {
            is NoSuchElementException -> NotFoundException(e.message)
            is ExposedSQLException -> when (PostgresErrors.by(e)) {
                PostgresErrors.ForeignKeyViolation -> UnprocessableEntityException(e.message)
                PostgresErrors.UniqueViolation -> ConflictException(e.message)
                else -> UnexpectedException(e.message)
            }
            else -> UnexpectedException(e.message)
        }.let {
            Result.failure(it)
        }
    }
}

inline fun FieldSet.isExisting(where: SqlExpressionBuilder.() -> Op<Boolean>) =
    select(SqlExpressionBuilder.where()).count() != 0L
