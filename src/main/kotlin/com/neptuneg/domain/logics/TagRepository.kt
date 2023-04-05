package com.neptuneg.domain.logics

import com.neptuneg.domain.entities.Tag

interface TagRepository {
    fun getTags(): Result<List<Tag>>
}
