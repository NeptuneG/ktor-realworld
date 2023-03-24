package com.neptuneg.adaptor.database

import com.neptuneg.Database
import com.neptuneg.adaptor.database.gateway.repository.FollowingRepositoryImpl
import com.neptuneg.domain.logic.FollowingRepository
import org.koin.dsl.module

val databaseKoins = module {
    single<Database> { ExposedPostgres(get()) }
    single<FollowingRepository> { FollowingRepositoryImpl() }
}
