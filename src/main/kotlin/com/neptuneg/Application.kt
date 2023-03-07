package com.neptuneg

import com.neptuneg.adaptor.database.DatabaseManager
import com.neptuneg.adaptor.web.Server
import com.neptuneg.config.Config
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

fun main() {
    val config = ConfigLoaderBuilder.default()
        .addResourceSource("/config.yaml")
        .build()
        .loadConfigOrThrow<Config>()

    DatabaseManager.connect(config.database)
    DatabaseManager.migrate()
    Server.serve(config.server.port)
}
