package com.neptuneg.adaptor.database.gateway.repository

import com.neptuneg.adaptor.database.gateway.entity.FollowingEntity
import com.neptuneg.adaptor.database.gateway.extension.runTxCatching
import com.neptuneg.adaptor.database.gateway.table.FollowingsTable
import com.neptuneg.domain.entity.Following
import com.neptuneg.domain.entity.User
import com.neptuneg.domain.logic.FollowingRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class FollowingRepositoryImpl: FollowingRepository {
    override fun isExisting(following: Following): Result<Boolean> {
        return runTxCatching {
            FollowingsTable.select { by(following) }.count() != 0L
        }
    }

    override fun create(following: Following): Result<Unit> {
        return runTxCatching {
            FollowingEntity.new {
                followerId = following.followerId
                followeeId = following.followeeId
            }
        }
    }

    override fun delete(following: Following): Result<Unit> {
        return runTxCatching {
            FollowingEntity.find { by(following) }.first().delete()
        }
    }

    override fun findFolloweeIds(follower: User): Result<List<UUID>> {
        return runTxCatching {
            FollowingsTable
                .slice(FollowingsTable.followeeId)
                .select { FollowingsTable.followerId.eq(follower.id) }
                .map { it[FollowingsTable.followeeId] }
        }
    }

    private fun SqlExpressionBuilder.by(following: Following) =
        (FollowingsTable.followerId eq following.followerId)
            .and(FollowingsTable.followeeId eq following.followeeId)
}
