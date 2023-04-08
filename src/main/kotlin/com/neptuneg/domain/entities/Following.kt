package com.neptuneg.domain.entities

import com.neptuneg.infrastructure.exceptions.ValidationException
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
