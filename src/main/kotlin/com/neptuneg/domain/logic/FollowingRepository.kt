package com.neptuneg.domain.logic

import com.neptuneg.domain.entity.Following
import com.neptuneg.domain.entity.User
import java.util.*

interface FollowingRepository {
    fun isExisting(followerId: UUID, followeeId: UUID): Result<Boolean>
    fun create(following: Following): Result<Unit>
    fun delete(following: Following): Result<Unit>
    fun findFolloweeIds(follower: User): Result<List<UUID>>
}
