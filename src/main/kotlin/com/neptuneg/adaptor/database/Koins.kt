package com.neptuneg.adaptor.database

import com.neptuneg.Database
import com.neptuneg.adaptor.database.gateway.repository.*
import com.neptuneg.domain.logic.*
import org.koin.dsl.module

val databaseKoins = module {
    single<Database> { ExposedPostgres(get()) }
    single<FollowingRepository> { FollowingRepositoryImpl() }
    single<ArticleRepository> { ArticleRepositoryImpl(get(), get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
    single<CommentRepository> { CommentRepositoryImpl(get(), get()) }
    single<TagRepository> { TagRepositoryImpl() }
}
