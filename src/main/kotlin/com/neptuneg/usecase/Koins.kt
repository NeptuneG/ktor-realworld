package com.neptuneg.usecase

import com.neptuneg.usecase.inputport.ProfileUseCase
import com.neptuneg.usecase.inputport.UserUseCase
import com.neptuneg.usecase.interator.ProfileUseCaseImpl
import com.neptuneg.usecase.interator.UserUseCaseImpl
import org.koin.dsl.module

val useCaseKoins = module {
    single<UserUseCase> { UserUseCaseImpl(get()) }
    single<ProfileUseCase> { ProfileUseCaseImpl(get()) }
}
