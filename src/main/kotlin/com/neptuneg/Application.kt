package com.neptuneg

import com.neptuneg.adaptor.database.databaseKoins
import com.neptuneg.adaptor.keycloak.keycloakKoins
import com.neptuneg.adaptor.web.Server
import com.neptuneg.adaptor.web.webKoins
import com.neptuneg.config.configKoins
import com.neptuneg.usecase.useCaseKoins
import org.jetbrains.exposed.sql.Database
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.logger.slf4jLogger
import javax.sql.DataSource

fun main() {
    insertKoins()

    connectDatabase()
    serve()
}

fun insertKoins(resource: String = "/config.yaml") = startKoin {
    slf4jLogger()
    modules(
        configKoins(resource),
        databaseKoins,
        webKoins,
        keycloakKoins,
        useCaseKoins,
    )
}

fun connectDatabase() {
    val dataSource: DataSource = getKoin().get()
    Database.connect(dataSource)
}

fun serve() {
    val server: Server = getKoin().get()
    server.serve()
}
