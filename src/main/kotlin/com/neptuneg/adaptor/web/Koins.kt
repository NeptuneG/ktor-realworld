package com.neptuneg.adaptor.web

import com.neptuneg.Server
import org.koin.dsl.module

val webKoins = module {
    single<Server> { Ktor(get()) }
}
