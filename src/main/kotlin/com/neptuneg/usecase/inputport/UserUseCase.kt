package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.User

interface UserUseCase {
    fun create(username: String, email: String, password: String): Result<User>
    fun getByToken(token: String): Result<User>
    fun getByUsername(username: String): Result<User>
    fun update(userId: String, userAttributes: Map<String, String?>): Result<Unit>
    fun requestToken(email: String, password: String): Result<String>
}
