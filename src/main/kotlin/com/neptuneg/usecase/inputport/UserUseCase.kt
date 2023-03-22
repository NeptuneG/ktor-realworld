package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.User

interface UserUseCase {
    suspend fun create(user: User): String
    suspend fun read(id: Int): User?
    suspend fun update(user: User, userId: String)
    suspend fun delete(id: Int)
    suspend fun requestToken(user: User): String
}
