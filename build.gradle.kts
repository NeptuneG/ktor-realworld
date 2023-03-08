object versions {
    const val ktor = "2.2.3"
    const val kotlin = "1.8.10"
    const val logback = "1.2.11"
    const val postgres = "42.5.1"
    const val h2 = "2.1.214"
    const val exposed = "0.41.1"
    const val prometheus = "1.6.3"
    const val koin = "3.3.3"
    const val koinKtor = "3.3.1"
    const val hoplite = "2.7.2"
    const val hikariCP = "5.0.1"
}

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

group = "com.neptuneg"
version = "0.0.1"
application {
    mainClass.set("com.neptuneg.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-call-logging-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-core-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-netty-jvm:${versions.ktor}")
    testImplementation("io.ktor:ktor-server-tests-jvm:${versions.ktor}")

    implementation("io.insert-koin:koin-core:${versions.koin}")
    implementation("io.insert-koin:koin-ktor:${versions.koinKtor}")
    implementation("io.insert-koin:koin-logger-slf4j:${versions.koinKtor}")
    testImplementation("io.insert-koin:koin-test:${versions.koin}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${versions.kotlin}")

    implementation("org.postgresql:postgresql:${versions.postgres}")
    implementation("com.h2database:h2:${versions.h2}")

    implementation("org.jetbrains.exposed:exposed-core:${versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${versions.exposed}")
    implementation("com.zaxxer:HikariCP:${versions.hikariCP}")

    implementation("io.micrometer:micrometer-registry-prometheus:${versions.prometheus}")

    implementation("ch.qos.logback:logback-classic:${versions.logback}")

    implementation("com.sksamuel.hoplite:hoplite-core:${versions.hoplite}")
    implementation("com.sksamuel.hoplite:hoplite-yaml:${versions.hoplite}")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}
