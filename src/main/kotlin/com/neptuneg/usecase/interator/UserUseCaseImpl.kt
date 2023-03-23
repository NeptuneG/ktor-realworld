package com.neptuneg.usecase.interator

import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import com.neptuneg.domain.entity.User
import com.neptuneg.domain.entity.UserInfo
import com.neptuneg.usecase.inputport.UserUseCase

class UserUseCaseImpl(
    private val keycloakService: KeycloakService
) : UserUseCase {
    override suspend fun create(user: User, password: String): Result<User> {
        return keycloakService.createUser(user, password)
    }

    override suspend fun read(token: String): Result<User> {
        return keycloakService.getUserInfo(token).map { it.toUser() }
    }

    override suspend fun update(userId: String, userAttributes: UserUseCase.UserAttributes): Result<Unit> {
        return keycloakService.updateUser(userId, KeycloakService.UserAttributes(
            email = userAttributes.email,
            username = userAttributes.username,
            password = userAttributes.password,
            bio = userAttributes.bio,
            image = userAttributes.image
        ))
    }

    override suspend fun requestToken(email: String, password: String): Result<String> {
        return keycloakService.requestToken(email, password)
    }

    private fun UserInfo.toUser(): User {
        return User(
            username = preferredUsername,
            email = email,
            bio = bio,
            image = image,
        )
    }
}
