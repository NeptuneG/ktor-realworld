package com.neptuneg.adaptor.database

import com.neptuneg.adaptor.database.gateway.repository.FollowingRepositoryImpl
import com.neptuneg.domain.logic.FollowingRepository
import org.koin.dsl.module

val databaseKoins = module {
    single { buildHikariDataSource(get()) }
    single<FollowingRepository> { FollowingRepositoryImpl() }
}
