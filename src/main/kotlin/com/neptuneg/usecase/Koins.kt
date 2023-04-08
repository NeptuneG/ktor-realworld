package com.neptuneg.usecase

import com.neptuneg.usecase.inputport.UserUseCase
import com.neptuneg.usecase.inputport.ProfileUseCase
import com.neptuneg.usecase.inputport.ArticleUseCase
import com.neptuneg.usecase.inputport.CommentUseCase
import com.neptuneg.usecase.inputport.TagUseCase
import com.neptuneg.usecase.interator.UserUseCaseImpl
import com.neptuneg.usecase.interator.ProfileUseCaseImpl
import com.neptuneg.usecase.interator.ArticleUseCaseImpl
import com.neptuneg.usecase.interator.CommentUseCaseImpl
import com.neptuneg.usecase.interator.TagUseCaseImpl
import org.koin.dsl.module

val useCaseKoins = module {
    single<UserUseCase> { UserUseCaseImpl(get()) }
    single<ProfileUseCase> { ProfileUseCaseImpl(get(), get()) }
    single<ArticleUseCase> { ArticleUseCaseImpl(get(), get()) }
    single<CommentUseCase> { CommentUseCaseImpl(get()) }
    single<TagUseCase> { TagUseCaseImpl(get()) }
}
