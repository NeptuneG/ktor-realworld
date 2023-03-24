package com.neptuneg.adaptor.web

import com.neptuneg.Server
import com.neptuneg.usecase.inputport.Sample
import com.neptuneg.usecase.interator.SampleImpl
import org.koin.dsl.module

val webKoins = module {
    single<Sample> { SampleImpl() }
    single<Server> { Ktor(get()) }
}
