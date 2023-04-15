package com.neptuneg.adaptor.database

import com.neptuneg.Database
import com.neptuneg.adaptor.database.gateway.repositories.ArticleRepositoryImpl
import com.neptuneg.adaptor.database.gateway.repositories.CommentRepositoryImpl
import com.neptuneg.adaptor.database.gateway.repositories.FollowingRepositoryImpl
import com.neptuneg.adaptor.database.gateway.repositories.TagRepositoryImpl
import com.neptuneg.adaptor.keycloak.repositories.UserRepositoryImpl
import com.neptuneg.domain.logics.ArticleRepository
import com.neptuneg.domain.logics.CommentRepository
import com.neptuneg.domain.logics.FollowingRepository
import com.neptuneg.domain.logics.TagRepository
import com.neptuneg.domain.logics.UserRepository
import org.koin.dsl.module

val databaseKoins = module {
    single<Database> { ExposedPostgres(get()) }
    single<FollowingRepository> { FollowingRepositoryImpl() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<TagRepository> { TagRepositoryImpl() }
}
