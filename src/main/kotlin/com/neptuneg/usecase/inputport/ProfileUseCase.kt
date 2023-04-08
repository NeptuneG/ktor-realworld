package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entities.Profile
import com.neptuneg.domain.entities.User

interface ProfileUseCase {
    fun get(follower: User?, followee: User): Result<Profile>
    fun follow(follower: User, followee: User): Result<Profile>
    fun unfollow(follower: User, followee: User): Result<Profile>
    fun findFollowees(follower: User): Result<List<Profile>>
}
