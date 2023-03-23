package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.User

interface UserUseCase {
    data class UserAttributes(
        val email: String? = null,
        val password: String? = null,
        val username: String? = null,
        val bio: String? = null,
        val image: String? = null
    )

    suspend fun create(user: User, password: String): Result<User>
    suspend fun read(token: String): Result<User>
    suspend fun update(userId: String, userAttributes: UserAttributes): Result<Unit>
    suspend fun requestToken(email: String, password: String): Result<String>
}
