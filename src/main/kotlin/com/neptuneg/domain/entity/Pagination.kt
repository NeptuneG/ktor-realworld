package com.neptuneg.domain.entity

data class Pagination(
    val offset: Long = defaultOffset,
    val limit: Int = defaultLimit,
) {
    companion object {
        const val defaultOffset = 0L
        const val defaultLimit = 20
        val default = Pagination(defaultOffset, defaultLimit)
    }
}
