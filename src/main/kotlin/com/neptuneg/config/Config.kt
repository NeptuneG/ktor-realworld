package com.neptuneg.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

data class Server(
    val port: Int
)

data class Database(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val driverClassName: String,
)

data class Config(
    val server: Server,
    val database: Database
) {
    companion object {
        fun buildFromYamlResource(resource: String): Config {
            return ConfigLoaderBuilder.default().addResourceSource(resource).build().loadConfigOrThrow()
        }
    }
}
