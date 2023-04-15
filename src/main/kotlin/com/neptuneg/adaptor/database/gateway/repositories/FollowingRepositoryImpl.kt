package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.adaptor.database.gateway.extensions.runTxCatching
import com.neptuneg.adaptor.database.gateway.tables.FollowingsTable
import com.neptuneg.domain.logics.FollowingRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.util.*

class FollowingRepositoryImpl : FollowingRepository {
    override fun deleteByFollowingUserId(followingUserId: UUID): Result<Unit> {
        return runTxCatching {
            FollowingsTable.deleteWhere { FollowingsTable.followingUserId.eq(followingUserId) }
        }
    }

    override fun listFollowerIds(followingUserId: UUID): Result<List<UUID>> {
        return runTxCatching {
            FollowingsTable
                .slice(FollowingsTable.followerId)
                .select { FollowingsTable.followingUserId.eq(followingUserId) }
                .map { it[FollowingsTable.followerId] }
        }
    }

    override fun listFollowingUserIds(followerId: UUID): Result<List<UUID>> {
        return runTxCatching {
            FollowingsTable
                .slice(FollowingsTable.followerId)
                .select { FollowingsTable.followingUserId.eq(followerId) }
                .map { it[FollowingsTable.followerId] }
        }
    }

    override fun batchCreate(followingUserId: UUID, followerIds: List<UUID>): Result<Unit> {
        return runTxCatching {
            FollowingsTable.batchInsert(followerIds) {
                this[FollowingsTable.followerId] = it
                this[FollowingsTable.followingUserId] = followingUserId
            }
        }
    }
}
