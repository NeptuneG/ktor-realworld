package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.User

interface UserUseCase {
    suspend fun create(username: String, email: String, password: String): Result<User>
    suspend fun getByToken(token: String): Result<User>
    suspend fun getByUsername(username: String): Result<User>
    suspend fun update(userId: String, userAttributes: Map<String, String?>): Result<Unit>
    suspend fun requestToken(email: String, password: String): Result<String>
}
