package com.neptuneg

import com.neptuneg.adaptor.database.DatabaseManager
import com.neptuneg.adaptor.web.Server
import com.neptuneg.config.Config

fun main() {
    val config = Config.buildFromYamlResource("/config.yaml")

    DatabaseManager.connect(config.database)
    Server.serve(config.server.port)
}
