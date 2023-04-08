package com.neptuneg.usecase.interator

import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entities.Following
import com.neptuneg.domain.entities.Profile
import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.FollowingRepository
import com.neptuneg.usecase.inputport.ProfileUseCase
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class ProfileUseCaseImpl(
    private val followingRepository: FollowingRepository,
    private val keycloakService: KeycloakService,
) : ProfileUseCase {
    override fun get(follower: User?, followee: User): Result<Profile> {
        return runCatching {
            val isFollowing = follower?.let { follower ->
                followingRepository.isExisting(follower.id, followee.id).getOrThrow()
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
                keycloakService.findUser(followeeId).map { it.profile(true) }.getOrThrow()
            }
        }
    }
}
