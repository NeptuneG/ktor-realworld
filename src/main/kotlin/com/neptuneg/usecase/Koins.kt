package com.neptuneg.usecase

import com.neptuneg.usecase.inputport.UserUseCase
import com.neptuneg.usecase.interator.UserUseCaseByKeycloak
import org.koin.dsl.module

val useCaseKoins = module {
//    single<UserUseCase> { UserUseCaseByDatabase(get()) }
    single<UserUseCase> { UserUseCaseByKeycloak(get()) }
}
