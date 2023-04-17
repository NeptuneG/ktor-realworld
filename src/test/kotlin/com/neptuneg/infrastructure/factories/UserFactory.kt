package com.neptuneg.infrastructure.factories

import com.neptuneg.domain.entities.User
import java.util.*

object UserFactory {
    fun build(id: UUID = UUID.randomUUID()): User {
        val username = faker.color.unique.name()
        val email = "$username@${faker.animal.name()}.com"
        return User(
            id = id,
            username = username,
            email = email,
            bio = faker.lorem.words(),
            image = "${faker.animal.name()}.jpg",
            followerIds = mutableListOf(),
            followingUserIds = mutableListOf()
        )
    }
}
