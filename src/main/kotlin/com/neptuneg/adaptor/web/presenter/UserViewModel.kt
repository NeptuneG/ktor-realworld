package com.neptuneg.adaptor.web.presenter

import com.neptuneg.autogen.model.User
import com.neptuneg.domain.entity.User as DomainUser
import com.neptuneg.autogen.model.Login200Response

object UserViewModel {
    operator fun invoke(user: DomainUser, token: String): Login200Response {
        return Login200Response(
            user = User(
                email = user.email,
                token = token,
                username = user.username,
                bio = user.bio,
                image = user.image,
            )
        )
    }
}
