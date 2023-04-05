package com.neptuneg.domain.logics

import com.neptuneg.domain.entities.Following
import com.neptuneg.domain.entities.User
import java.util.*

interface FollowingRepository {
    fun isExisting(followerId: UUID, followeeId: UUID): Result<Boolean>
    fun create(following: Following): Result<Unit>
    fun delete(following: Following): Result<Unit>
    fun findFolloweeIds(follower: User): Result<List<UUID>>
}
