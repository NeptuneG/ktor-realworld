package com.neptuneg.adaptor.web

import com.neptuneg.adaptor.database.gateway.UserRepositoryImpl
import com.neptuneg.domain.logic.UserRepository
import com.neptuneg.usecase.inputport.Sample
import com.neptuneg.usecase.inputport.UserUseCase
import com.neptuneg.usecase.interator.SampleImpl
import com.neptuneg.usecase.interator.UserUseCaseImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.insertKoins() {
    val module = module {
        single<Sample> { SampleImpl() }
        single<UserRepository> { UserRepositoryImpl() }
        single<UserUseCase> { UserUseCaseImpl(get()) }
    }

    install(Koin) {
        slf4jLogger()
        modules(module)
    }
}
