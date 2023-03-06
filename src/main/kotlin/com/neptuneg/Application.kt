package com.neptuneg

import com.neptuneg.adaptor.database.DatabaseManager
import com.neptuneg.adaptor.web.Server

fun main() {
    DatabaseManager.connect(
        database = System.getenv("REALWORLD_DATABASE_NAME"),
        user = System.getenv("REALWORLD_DATABASE_USER"),
        password = System.getenv("REALWORLD_DATABASE_PASSWORD")
    )
    DatabaseManager.migrate()
    Server.serve(8080)
}
