package com.neptuneg.usecase.interator

import com.neptuneg.domain.entity.Following
import com.neptuneg.domain.entity.Profile
import com.neptuneg.domain.entity.User
import com.neptuneg.domain.logic.FollowingRepository
import com.neptuneg.usecase.inputport.ProfileUseCase

class ProfileUseCaseImpl(
    private val followingRepository: FollowingRepository,
): ProfileUseCase {
    override suspend fun get(follower: User?, followee: User): Result<Profile> {
        return runCatching {
            val isFollowing = follower?.let { follower ->
                val following = Following.new(followerId = follower.id, followeeId = followee.id)
                followingRepository.isExisting(following).getOrThrow()
            } ?: false

            followee.buildProfile(isFollowing)
        }
    }

    override suspend fun follow(follower: User, followee: User): Result<Profile> {
        return runCatching {
            val following = Following.new(followerId = follower.id, followeeId = followee.id)
            followingRepository.create(following).getOrThrow()
            followee.buildProfile(true)
        }
    }

    override suspend fun unfollow(follower: User, followee: User): Result<Profile> {
        return runCatching {
            val following = Following.new(followerId = follower.id, followeeId = followee.id)
            followingRepository.delete(following).getOrThrow()
            followee.buildProfile(false)
        }
    }
}
