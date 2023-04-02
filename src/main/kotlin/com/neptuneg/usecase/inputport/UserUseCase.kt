package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.User
import java.util.*

interface UserUseCase {
    fun create(username: String, email: String, password: String): Result<User>
    fun findByToken(token: String): Result<User>
    fun findByUsername(username: String): Result<User>
    fun update(userId: UUID, userAttributes: Map<String, String?>): Result<Unit>
    fun requestToken(email: String, password: String): Result<String>
}
