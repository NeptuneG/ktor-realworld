package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.GetProfileByUsername200Response
import com.neptuneg.autogen.model.Profile
import com.neptuneg.domain.entities.Profile as DomainProfile

object ProfileViewModel {
    operator fun invoke(profile: DomainProfile) = GetProfileByUsername200Response(profile = profile.toView())
}

internal fun DomainProfile.toView() = Profile(
    username = user.username,
    bio = user.bio,
    image = user.image,
    following = following
)
