object versions {
    const val ktor = "2.2.3"
    const val kotlin = "1.8.10"
    const val logback = "1.2.11"
    const val postgres = "42.5.1"
    const val exposed = "0.41.1"
    const val koin = "3.3.3"
    const val hoplite = "2.7.2"
    const val hikariCP = "5.0.1"
    const val auth0Jwt = "4.3.0"
    const val keycloak = "21.0.1"
    const val moshi = "1.13.0"
    const val okhttp = "4.10.0"
    const val kotest = "5.5.5"
    const val mockk = "1.13.4"
    const val flyway = "9.16.3"
    const val faker = "1.14.0"
}

plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.3"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("org.openapi.generator") version "6.6.0"
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
    implementation("io.ktor:ktor-server-call-logging-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-core-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-netty-jvm:${versions.ktor}")
    implementation("io.ktor:ktor-server-auth:${versions.ktor}")
    implementation("io.ktor:ktor-server-auth-jwt:${versions.ktor}")
    implementation("io.ktor:ktor-client-apache:${versions.ktor}")
    implementation("io.ktor:ktor-server-sessions:${versions.ktor}")
    implementation("io.ktor:ktor-server-cors:${versions.ktor}")
    testImplementation("io.ktor:ktor-server-tests-jvm:${versions.ktor}")

    testImplementation("io.kotest:kotest-runner-junit5:${versions.kotest}")
    testImplementation("io.kotest:kotest-assertions-core:${versions.kotest}")
    testImplementation("io.mockk:mockk:${versions.mockk}")

    implementation("com.auth0:java-jwt:${versions.auth0Jwt}")
    implementation("org.keycloak:keycloak-admin-client:${versions.keycloak}")

    implementation("com.squareup.moshi:moshi-kotlin:${versions.moshi}")
    implementation("com.squareup.moshi:moshi-adapters:${versions.moshi}")

    implementation("com.squareup.okhttp3:okhttp:${versions.okhttp}")

    implementation("io.insert-koin:koin-core:${versions.koin}")
    testImplementation("io.insert-koin:koin-test:${versions.koin}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${versions.kotlin}")

    implementation("org.postgresql:postgresql:${versions.postgres}")
    testImplementation("org.flywaydb:flyway-core:${versions.flyway}")
    testImplementation("io.github.serpro69:kotlin-faker:${versions.faker}")

    implementation("org.jetbrains.exposed:exposed-core:${versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-dao:${versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${versions.exposed}")
    implementation("com.zaxxer:HikariCP:${versions.hikariCP}")

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

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$rootDir/spec/openapi.yml")
    outputDir.set("$rootDir/gen")
    apiPackage.set("$group.autogen.api")
    invokerPackage.set("$group.autogen.invoker")
    modelPackage.set("$group.autogen.model")
    configOptions.set(mapOf("dateLibrary" to "java8"))
}

sourceSets.main {
    java.srcDirs("$rootDir/gen/src/main/kotlin/com/neptuneg/autogen/model")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
