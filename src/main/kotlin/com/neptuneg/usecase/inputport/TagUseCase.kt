package com.neptuneg.usecase.inputport

import com.neptuneg.domain.entities.Tag

interface TagUseCase {
    fun getTags(): Result<List<Tag>>
}
