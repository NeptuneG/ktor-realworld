package com.neptuneg.usecase

import com.neptuneg.usecase.inputport.*
import com.neptuneg.usecase.interator.*
import org.koin.dsl.module

val useCaseKoins = module {
    single<UserUseCase> { UserUseCaseImpl(get()) }
    single<ProfileUseCase> { ProfileUseCaseImpl(get(), get()) }
    single<ArticleUseCase> { ArticleUseCaseImpl(get(), get()) }
    single<CommentUseCase> { CommentUseCaseImpl(get()) }
    single<TagUseCase> { TagUseCaseImpl(get()) }
}
