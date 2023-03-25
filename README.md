# Ktor-Realworld

## Specification

- [gothinkster/realworld](https://github.com/gothinkster/realworld)
  - [Introduction | RealWorld](https://realworld-docs.netlify.app/docs/specs/backend-specs/introduction/)

## Architecture

- [The Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

## Dependencies

- [Keycloak](https://www.keycloak.org/)
- [Ktor](https://github.com/ktorio/ktor)
- [Koin](https://github.com/InsertKoinIO/koin)
- [Moshi](https://github.com/square/moshi)
- [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator)
- [Exposed](https://github.com/JetBrains/Exposed)
- [HikariCP](https://github.com/brettwooldridge/HikariCP)
- [Flyway](https://github.com/flyway/flyway/)
- [Kotest](https://github.com/kotest/kotest)

## Commands

- Export Keycloak realm's settings

```bash
docker-compose exec keycloak /opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/import --realm real-world
```

## References
