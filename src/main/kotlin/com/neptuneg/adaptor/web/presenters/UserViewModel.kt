package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.Login200Response
import com.neptuneg.autogen.model.User
import com.neptuneg.domain.entities.User as DomainUser

object UserViewModel {
    operator fun invoke(user: DomainUser) = Login200Response(
        user = User(
            email = user.email,
            token = user.token!!,
            username = user.username,
            bio = user.bio,
            image = user.image,
        )
    )
    operator fun invoke(user: DomainUser, token: String) = Login200Response(
        user = User(
            email = user.email,
            token = token,
            username = user.username,
            bio = user.bio,
            image = user.image,
        )
    )
}
