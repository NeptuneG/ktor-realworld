package com.neptuneg.usecase.interator

import com.neptuneg.domain.entity.User
import com.neptuneg.domain.logic.UserRepository
import com.neptuneg.usecase.inputport.UserUseCase

class UserUseCaseImpl(
    private val userRepository: UserRepository
) : UserUseCase {
    override suspend fun create(user: User): Int {
        return userRepository.create(user)
    }

    override suspend fun read(id: Int): User? {
        return userRepository.read(id)
    }

    override suspend fun update(id: Int, user: User) {
        return userRepository.update(id, user)
    }

    override suspend fun delete(id: Int) {
        return userRepository.delete(id)
    }
}
