package com.neptuneg.domain.entity

import java.util.UUID

data class Following (
    val followerId: UUID,
    val followeeId: UUID,
) {
    init {
        if (followerId == followeeId) { throw Exception("followerId can not be equal to followeeId") }
    }
}
