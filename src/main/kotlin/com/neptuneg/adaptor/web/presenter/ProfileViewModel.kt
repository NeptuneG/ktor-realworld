package com.neptuneg.adaptor.web.presenter

import com.neptuneg.autogen.model.GetProfileByUsername200Response
import com.neptuneg.autogen.model.Profile
import com.neptuneg.domain.entity.Profile as DomainProfile

object ProfileViewModel {
    operator fun invoke(profile: DomainProfile): GetProfileByUsername200Response {
        return GetProfileByUsername200Response(
            profile = Profile(
                username = profile.username,
                bio = profile.bio,
                image = profile.image,
                following = profile.following
            )
        )
    }
}
