package com.neptuneg.config

import org.koin.dsl.module

fun configKoins(resource: String) = module {
    val config = Config.buildFromYamlResource(resource)

    single { config }
    single { config.database }
    single { config.server }
    single { config.server.keycloak }
}

