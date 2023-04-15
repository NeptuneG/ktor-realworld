package com.neptuneg.domain.logics

import java.util.*

interface FollowingRepository {
    fun deleteByFollowingUserId(followingUserId: UUID): Result<Unit>
    fun listFollowerIds(followingUserId: UUID): Result<List<UUID>>
    fun listFollowingUserIds(followerId: UUID): Result<List<UUID>>
    fun batchCreate(followingUserId: UUID, followerIds: List<UUID>): Result<Unit>
}
