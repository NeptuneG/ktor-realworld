package com.neptuneg.usecase.interator

import com.neptuneg.domain.entity.Tag
import com.neptuneg.domain.logic.TagRepository
import com.neptuneg.usecase.inputport.TagUseCase

class TagUseCaseImpl(
    private val tagRepository: TagRepository
): TagUseCase {
    override fun getTags(): Result<List<Tag>> {
        return tagRepository.getTags()
    }
}
