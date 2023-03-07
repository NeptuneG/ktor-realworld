# Ktor-Realworld

## Specification

- [gothinkster/realworld](https://github.com/gothinkster/realworld)

## Dependencies

- [Ktor](https://github.com/ktorio/ktor)
- [Koin](https://github.com/InsertKoinIO/koin)
- [Moshi](https://github.com/square/moshi)
- [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator)
- [Exposed](https://github.com/JetBrains/Exposed)
- [HikariCP](https://github.com/brettwooldridge/HikariCP)
- [Flyway](https://github.com/flyway/flyway/)
- [Kotest](https://github.com/kotest/kotest)

## Architecture

- [The Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## References

- [JVMのtargetエラーが出ました](https://www.techback.info/kotlin-jvm-error/)
  - Additionally, the following snippet is also required in `build.gradle.kts`
  ```kotlin
  tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
  }
  ```
