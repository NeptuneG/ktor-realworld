package com.neptuneg.domain.logic

import com.neptuneg.domain.entity.Following

interface FollowingRepository {
    fun isExisting(following: Following): Result<Boolean>
    fun create(following: Following): Result<Unit>
    fun delete(following: Following): Result<Unit>
}
