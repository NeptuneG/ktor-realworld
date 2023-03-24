package com.neptuneg.usecase.interator

import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entity.User
import com.neptuneg.usecase.inputport.UserUseCase

class UserUseCaseImpl(
    private val keycloakService: KeycloakService
) : UserUseCase {
    override fun create(username: String, email: String, password: String): Result<User> {
        return keycloakService.createUser(username, email, password)
    }

    override fun getByToken(token: String): Result<User> {
        return keycloakService.getUser(token)
    }

    override fun getByUsername(username: String): Result<User> {
        return keycloakService.getUserByUsername(username)
    }

    override fun update(userId: String, userAttributes: Map<String, String?>): Result<Unit> {
        return keycloakService.updateUser(userId, userAttributes)
    }

    override fun requestToken(email: String, password: String): Result<String> {
        return keycloakService.requestToken(email, password)
    }
}
