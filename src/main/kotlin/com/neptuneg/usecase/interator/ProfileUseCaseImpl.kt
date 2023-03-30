package com.neptuneg.usecase.interator

import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entity.Following
import com.neptuneg.domain.entity.Profile
import com.neptuneg.domain.entity.User
import com.neptuneg.domain.logic.FollowingRepository
import com.neptuneg.usecase.inputport.ProfileUseCase

class ProfileUseCaseImpl(
    private val followingRepository: FollowingRepository,
    private val keycloakService: KeycloakService,
): ProfileUseCase {
    override fun get(follower: User?, followee: User): Result<Profile> {
        return runCatching {
            val isFollowing = follower?.let { follower ->
                followingRepository.isExisting(Following(follower, followee)).getOrThrow()
            } ?: false

            followee.profile(isFollowing)
        }
    }

    override fun follow(follower: User, followee: User): Result<Profile> {
        return followingRepository.create(Following(follower, followee)).mapCatching {
            followee.profile(true)
        }
    }

    override fun unfollow(follower: User, followee: User): Result<Profile> {
        return followingRepository.delete(Following(follower, followee)).mapCatching {
            followee.profile(false)
        }
    }

    override fun findFollowees(follower: User): Result<List<Profile>> {
        return followingRepository.findFolloweeIds(follower).mapCatching { followeeIds ->
            followeeIds.map { followeeId ->
                keycloakService
                    .findUser(followeeId)
                    .map { it.profile(true) }.getOrThrow()
            }
        }
    }
}
