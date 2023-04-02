package com.neptuneg.domain.logic

import com.neptuneg.domain.entity.Article
import com.neptuneg.domain.entity.User

interface FavoriteRepository {
    fun favoriteBySlug(user: User, slug: String): Result<Article>
    fun unfavoriteBySlug(user: User, slug: String): Result<Article>
}
