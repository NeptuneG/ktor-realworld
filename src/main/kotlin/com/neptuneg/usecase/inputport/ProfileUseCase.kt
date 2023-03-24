package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.Profile
import com.neptuneg.domain.entity.User

interface ProfileUseCase {
    suspend fun get(visitor: User?, username: String): Result<Profile>
    suspend fun follow(visitor: User, username: String): Result<Profile>
    suspend fun unfollow(visitor: User, username: String): Result<Profile>
}
