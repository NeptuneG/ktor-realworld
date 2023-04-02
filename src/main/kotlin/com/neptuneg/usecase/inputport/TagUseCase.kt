package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entity.Tag

interface TagUseCase {
    fun getTags(): Result<List<Tag>>
}
