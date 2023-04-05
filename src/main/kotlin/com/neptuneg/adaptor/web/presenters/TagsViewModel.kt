package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.GetTags200Response
import com.neptuneg.domain.entities.Tag

object TagsViewModel {
    operator fun invoke(tags: List<Tag>) = GetTags200Response(
        tags = tags.map { it.tag }.sorted()
    )
}
