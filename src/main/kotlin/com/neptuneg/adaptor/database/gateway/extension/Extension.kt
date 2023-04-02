package com.neptuneg.adaptor.database.gateway.extension

import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("TooGenericExceptionCaught")
fun <R> runTxCatching(block: () -> R): Result<R> {
    return try {
        transaction { Result.success(block()) }
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
