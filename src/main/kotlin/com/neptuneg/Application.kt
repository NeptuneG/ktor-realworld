package com.neptuneg

import com.neptuneg.plugins.configureDatabases
import com.neptuneg.plugins.configureMonitoring
import com.neptuneg.plugins.configureRouting
import com.neptuneg.plugins.configureSerialization
import com.neptuneg.sample.Runner
import com.neptuneg.sample.Sample
import com.neptuneg.sample.SampleImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(Netty, port = 8080) {
        module()
        koin()
    }.start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureRouting()
}

val koinModule = module {
    single<Sample> { SampleImpl() }
    single { Runner(get()) }
}

fun Application.koin() {
    install(Koin) {
        slf4jLogger()
        modules(koinModule)
    }
}
