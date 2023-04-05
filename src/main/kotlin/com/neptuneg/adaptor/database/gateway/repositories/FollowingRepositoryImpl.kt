package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.adaptor.database.gateway.entities.FollowingEntity
import com.neptuneg.adaptor.database.gateway.extensions.isExisting
import com.neptuneg.adaptor.database.gateway.extensions.runTxCatching
import com.neptuneg.adaptor.database.gateway.tables.FollowingsTable
import com.neptuneg.domain.entities.Following
import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.FollowingRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.util.*

class FollowingRepositoryImpl : FollowingRepository {
    override fun isExisting(followerId: UUID, followeeId: UUID): Result<Boolean> {
        return runTxCatching {
            FollowingsTable.isExisting { by(followerId, followeeId) }
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
    private fun SqlExpressionBuilder.by(followerId: UUID, followeeId: UUID) =
        (FollowingsTable.followerId eq followerId)
            .and(FollowingsTable.followeeId eq followeeId)
}
