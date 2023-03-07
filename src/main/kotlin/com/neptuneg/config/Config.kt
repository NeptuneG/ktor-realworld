package com.neptuneg.config

data class Server(
    val port: Int
)

data class Database(
    val host: String,
    val port: Int,
    val database: String,
    val user: String,
    val password: String
)

data class Config(
    val server: Server,
    val database: Database
)
