package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.Profile
import com.neptuneg.domain.entity.User

interface ProfileUseCase {
    suspend fun get(follower: User?, followee: User): Result<Profile>
    suspend fun follow(follower: User, followee: User): Result<Profile>
    suspend fun unfollow(follower: User, followee: User): Result<Profile>
}
