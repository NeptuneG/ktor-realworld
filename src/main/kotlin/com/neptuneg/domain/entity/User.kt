package com.neptuneg.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val age: Int)
