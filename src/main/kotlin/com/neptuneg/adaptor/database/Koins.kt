package com.neptuneg.adaptor.database

import org.koin.dsl.module

val databaseKoins = module {
    single { buildHikariDataSource(get()) }
}
