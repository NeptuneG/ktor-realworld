package com.neptuneg.adaptor.web.controllers

import com.neptuneg.adaptor.web.presenters.TagsViewModel
import com.neptuneg.usecase.inputport.TagUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

fun Route.tag() {
    val tagUseCase: TagUseCase by inject(TagUseCase::class.java)

    get("/tags") {
        val tags = tagUseCase.getTags().getOrThrow()
        call.respond(HttpStatusCode.OK, TagsViewModel(tags))
    }
}
