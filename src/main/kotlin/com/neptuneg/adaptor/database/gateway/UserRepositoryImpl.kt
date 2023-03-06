package com.neptuneg.adaptor.database.gateway

import com.neptuneg.adaptor.database.gateway.table.UserTable
import com.neptuneg.domain.entity.User
import com.neptuneg.domain.logic.UserRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

class UserRepositoryImpl : UserRepository {
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun create(user: User): Int = dbQuery {
        UserTable.insert {
            it[name] = user.name
            it[age] = user.age
        }[UserTable.id]
    }

    override suspend fun read(id: Int): User? {
        return dbQuery {
            UserTable.select { UserTable.id eq id }
                .map { User(it[UserTable.name], it[UserTable.age]) }
                .singleOrNull()
        }
    }

    override suspend fun update(id: Int, user: User) {
        dbQuery {
            UserTable.update({ UserTable.id eq id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    override suspend fun delete(id: Int) {
        dbQuery {
            UserTable.deleteWhere { UserTable.id.eq(id) }
        }
    }
}
