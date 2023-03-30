package com.neptuneg.domain.entity

import com.neptuneg.infrastructure.exception.ValidationException
import java.util.UUID

data class Following (
    val followerId: UUID,
    val followeeId: UUID,
) {
    init {
        if (followerId == followeeId) { throw ValidationException("followerId can not be equal to followeeId") }
    }

    constructor(follower: User, followee: User): this(follower.id, followee.id)
}
