package com.neptuneg.adaptor.database

import com.neptuneg.Database
import com.neptuneg.adaptor.database.gateway.repository.ArticleRepositoryImpl
import com.neptuneg.adaptor.database.gateway.repository.FavoriteRepositoryImpl
import com.neptuneg.adaptor.database.gateway.repository.FollowingRepositoryImpl
import com.neptuneg.domain.logic.ArticleRepository
import com.neptuneg.domain.logic.FavoriteRepository
import com.neptuneg.domain.logic.FollowingRepository
import org.koin.dsl.module

val databaseKoins = module {
    single<Database> { ExposedPostgres(get()) }
    single<FollowingRepository> { FollowingRepositoryImpl() }
    single<ArticleRepository> { ArticleRepositoryImpl(get(), get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
}
