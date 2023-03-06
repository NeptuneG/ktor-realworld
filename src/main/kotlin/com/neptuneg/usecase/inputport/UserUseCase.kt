package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.User

interface UserUseCase {
    suspend fun create(user: User): Int
    suspend fun read(id: Int): User?
    suspend fun update(id: Int, user: User)
    suspend fun delete(id: Int)
}
