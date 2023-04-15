package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.GetProfileByUsername200Response
import com.neptuneg.autogen.model.Profile
import java.util.*
import com.neptuneg.domain.entities.User as DomainUser

object ProfileViewModel {
    operator fun invoke(user: DomainUser, visitorId: UUID?) = GetProfileByUsername200Response(
        profile = user.toProfile(visitorId)
    )
}

internal fun DomainUser.toProfile(visitorId: UUID?) = Profile(
    username = username,
    bio = bio,
    image = image,
    following = visitorId?.let { followerIds.contains(visitorId) } ?: false
)
