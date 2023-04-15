package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entities.User
import java.util.*

interface UserUseCase {
    fun register(username: String, email: String, password: String): Result<User>
    fun login(email: String, password: String): Result<User>
    fun find(id: UUID): Result<User>
    fun find(username: String): Result<User>
    fun updateProfile(id: UUID, userAttributes: Map<String, String?>): Result<User>
    fun follow(followerId: UUID, followingUserName: String): Result<User>
    fun unfollow(followerId: UUID, followingUserName: String): Result<User>
}
