package com.neptuneg.domain.logics

import com.neptuneg.domain.entities.Article
import com.neptuneg.domain.entities.User

interface FavoriteRepository {
    fun favoriteBySlug(user: User, slug: String): Result<Article>
    fun unfavoriteBySlug(user: User, slug: String): Result<Article>
}
