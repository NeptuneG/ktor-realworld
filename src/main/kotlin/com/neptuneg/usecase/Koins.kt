package com.neptuneg.usecase

import com.neptuneg.usecase.inputport.ArticleUseCase
import com.neptuneg.usecase.inputport.AuthorizeUseCase
import com.neptuneg.usecase.inputport.CommentUseCase
import com.neptuneg.usecase.inputport.TagUseCase
import com.neptuneg.usecase.inputport.UserUseCase
import com.neptuneg.usecase.interator.ArticleUseCaseImpl
import com.neptuneg.usecase.interator.AuthorizeUseCaseImpl
import com.neptuneg.usecase.interator.CommentUseCaseImpl
import com.neptuneg.usecase.interator.TagUseCaseImpl
import com.neptuneg.usecase.interator.UserUseCaseImpl
import org.koin.dsl.module

val useCaseKoins = module {
    single<UserUseCase> { UserUseCaseImpl(get()) }
    single<ArticleUseCase> { ArticleUseCaseImpl(get()) }
    single<CommentUseCase> { CommentUseCaseImpl(get()) }
    single<TagUseCase> { TagUseCaseImpl(get()) }
    single<AuthorizeUseCase> { AuthorizeUseCaseImpl(get(), get()) }
}
