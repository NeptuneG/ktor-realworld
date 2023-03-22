package com.neptuneg.usecase.interator

import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entity.User
import com.neptuneg.usecase.inputport.UserUseCase

class UserUseCaseByKeycloak(
    private val keycloakService: KeycloakService
) : UserUseCase {
    override suspend fun create(user: User): String {
        keycloakService.createUser(user).getOrThrow()
        return keycloakService.requestToken(user).getOrThrow()
    }

    override suspend fun read(id: Int): User? {
        TODO("Not yet implemented")
    }

    override suspend fun update(user: User, userId: String) {
        keycloakService.updateUser(user, userId)
    }

    override suspend fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun requestToken(user: User): String {
        return keycloakService.requestToken(user).getOrThrow()
    }
}
