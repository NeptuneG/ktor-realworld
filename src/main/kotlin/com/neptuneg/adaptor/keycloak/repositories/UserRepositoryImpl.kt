package com.neptuneg.adaptor.keycloak.repositories

import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.FollowingRepository
import com.neptuneg.domain.logics.UserRepository
import java.util.*

@Suppress("TooManyFunctions")
class UserRepositoryImpl(
    private val keycloakService: KeycloakService,
    private val followingRepository: FollowingRepository,
) : UserRepository {
    override fun create(username: String, email: String, password: String): Result<User> {
        return keycloakService.createUser(username, email, password).mapCatching {
            val token = keycloakService.authenticate(email, password).getOrThrow()
            it.toUser(token)
        }
    }

    override fun authenticate(email: String, password: String): Result<User> {
        return runCatching {
            val token = keycloakService.authenticate(email, password).getOrThrow()
            keycloakService.findUserByToken(token).mapCatching { it.toUser(token) }.getOrThrow()
        }
    }

    override fun find(id: UUID): Result<User> {
        return keycloakService.findUserById(id).mapCatching { it.toUser() }
    }

    override fun find(username: String): Result<User> {
        return keycloakService.findUserByUsername(username).map { it.toUser() }
    }

    override fun updateProfile(user: User): Result<User> {
        return keycloakService.updateUser(user.toKeyCloakUserProfile()).mapCatching { user }
    }

    override fun updatePassword(id: UUID, password: String): Result<Unit> {
        return keycloakService.updateUserPassword(id, password)
    }

    override fun updateFollowers(user: User): Result<Unit> {
        return followingRepository.deleteByFollowingUserId(user.id).mapCatching {
            followingRepository.batchCreate(user.id, user.followerIds)
        }
    }

    private fun KeycloakService.KeyCloakUserProfile.toUser(token: String? = null) = User(
        id = id,
        username = username,
        email = email,
        bio = bio,
        image = image,
        token = token,
        followerIds = followingRepository.listFollowerIds(id).getOrThrow().toMutableList(),
        followingUserIds = followingRepository.listFollowingUserIds(id).getOrThrow().toMutableList(),
    )

    private fun User.toKeyCloakUserProfile() = KeycloakService.KeyCloakUserProfile(id, username, email, bio, image)
}
