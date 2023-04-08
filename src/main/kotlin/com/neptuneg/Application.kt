package com.neptuneg

import com.neptuneg.adaptor.database.databaseKoins
import com.neptuneg.adaptor.keycloak.keycloakKoins
import com.neptuneg.adaptor.web.webKoins
import com.neptuneg.infrastructure.config.configKoins
import com.neptuneg.usecase.useCaseKoins
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    insertKoins("/config.yaml")

    Database.connect()
    Server.serve()
}

fun insertKoins(resource: String = "/config.sample.yaml") = startKoin {
    modules(
        configKoins(resource),
        databaseKoins,
        webKoins,
        keycloakKoins,
        useCaseKoins,
    )
}

interface Database {
    fun connect()
    companion object {
        fun connect() = getKoin().get<Database>().connect()
    }
}

interface Server {
    fun serve()
    companion object {
        fun serve() = getKoin().get<Server>().serve()
    }
}
