package com.neptuneg.domain.logics

import com.neptuneg.domain.entities.User
import java.util.*

interface UserRepository {
    fun create(username: String, email: String, password: String): Result<User>
    fun authenticate(email: String, password: String): Result<User>
    fun find(id: UUID): Result<User>
    fun find(username: String): Result<User>
    fun updateProfile(user: User): Result<User>
    fun updatePassword(id: UUID, password: String): Result<Unit>
    fun updateFollowers(user: User): Result<Unit>
}
