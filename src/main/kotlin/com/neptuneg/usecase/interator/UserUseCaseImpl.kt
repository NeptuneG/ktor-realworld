package com.neptuneg.usecase.interator

import com.neptuneg.domain.entities.User
import com.neptuneg.domain.logics.UserRepository
import com.neptuneg.usecase.inputport.UserUseCase
import java.util.*

class UserUseCaseImpl(
    private val userRepository: UserRepository
) : UserUseCase {
    override fun register(username: String, email: String, password: String): Result<User> {
        return userRepository.create(username, email, password)
    }

    override fun login(email: String, password: String): Result<User> {
        return userRepository.authenticate(email, password)
    }

    override fun find(id: UUID): Result<User> {
        return userRepository.find(id)
    }

    override fun find(username: String): Result<User> {
        return userRepository.find(username)
    }

    override fun updateProfile(id: UUID, userAttributes: Map<String, String?>): Result<User> {
        return runCatching {
            val user = userRepository.find(id).getOrThrow()
            userAttributes["username"]?.apply { user.username = this }
            userAttributes["email"]?.apply { user.email = this }
            userAttributes["bio"]?.apply { user.bio = this }
            userAttributes["image"]?.apply { user.image = this }
            return userRepository.updateProfile(user).apply {
                userAttributes["password"]?.apply {
                    userRepository.updatePassword(id, this).getOrThrow()
                }
            }
        }
    }

    override fun follow(followerId: UUID, followingUserName: String): Result<User> {
        return runCatching {
            userRepository.find(followingUserName).getOrThrow().apply {
                followerIds.add(followerId)
                userRepository.updateFollowers(this).getOrThrow()
            }
        }
    }

    override fun unfollow(followerId: UUID, followingUserName: String): Result<User> {
        return runCatching {
            userRepository.find(followingUserName).getOrThrow().apply {
                followerIds.remove(followerId)
                userRepository.updateFollowers(this).getOrThrow()
            }
        }
    }
}
