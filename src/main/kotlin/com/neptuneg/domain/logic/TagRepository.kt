package com.neptuneg.domain.logic

import com.neptuneg.domain.entity.Tag

interface TagRepository {
    fun getTags(): Result<List<Tag>>
}
