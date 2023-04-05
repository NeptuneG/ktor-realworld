package com.neptuneg.adaptor.web.presenters

import com.neptuneg.autogen.model.User
import com.neptuneg.domain.entities.User as DomainUser
import com.neptuneg.autogen.model.Login200Response

object UserViewModel {
    operator fun invoke(user: DomainUser, token: String) = Login200Response(user = user.toView(token))
}

internal fun DomainUser.toView(token: String) = User(
    email = email,
    token = token,
    username = username,
    bio = bio,
    image = image,
)
