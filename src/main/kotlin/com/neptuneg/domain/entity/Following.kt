package com.neptuneg.domain.entity

import java.util.UUID

data class Following (
    val followerId: UUID,
    val followeeId: UUID,
) {
    companion object {
        fun new(followerId: UUID, followeeId: UUID): Following {
            return if (followerId == followeeId) {
                throw Exception("followerId can not be equal to followeeId")
            } else {
                Following(followerId, followeeId)
            }
        }
    }
}
