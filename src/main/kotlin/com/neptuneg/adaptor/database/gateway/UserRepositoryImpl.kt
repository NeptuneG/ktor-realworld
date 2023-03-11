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
            it[username] = user.username
            it[email] = user.email
            it[password] = user.password
            it[bio] = user.bio
            it[image] = user.image
        }[UserTable.id]
    }

    override suspend fun read(id: Int): User? {
        return dbQuery {
            UserTable.select { UserTable.id eq id }
                .map {
                    User(
                        it[UserTable.username],
                        it[UserTable.email],
                        it[UserTable.password],
                        it[UserTable.bio],
                        it[UserTable.image],
                        it[UserTable.createdAt],
                        it[UserTable.updatedAt],
                    )
                }.singleOrNull()
        }
    }

    override suspend fun update(id: Int, user: User) {
        dbQuery {
            UserTable.update({ UserTable.id eq id }) {
                it[username] = user.username
                it[email] = user.email
                it[password] = user.password
                it[bio] = user.bio
                it[image] = user.image
            }
        }
    }

    override suspend fun delete(id: Int) {
        dbQuery {
            UserTable.deleteWhere { UserTable.id.eq(id) }
        }
    }
}
