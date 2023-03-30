package com.neptuneg.adaptor.web.presenter

import com.neptuneg.autogen.model.User
import com.neptuneg.domain.entity.User as DomainUser
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
