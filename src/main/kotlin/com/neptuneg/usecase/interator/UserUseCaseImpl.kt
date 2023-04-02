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

    override fun findByToken(token: String): Result<User> {
        return keycloakService.findUserByToken(token)
    }

    override fun findByUsername(username: String): Result<User> {
        return keycloakService.findUserByUsername(username)
    }

    override fun update(userId: String, userAttributes: Map<String, String?>): Result<Unit> {
        return keycloakService.updateUser(userId, userAttributes)
    }

    override fun requestToken(email: String, password: String): Result<String> {
        return keycloakService.requestToken(email, password)
    }
}
