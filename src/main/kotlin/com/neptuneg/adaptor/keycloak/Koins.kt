package com.neptuneg.adaptor.keycloak

import com.neptuneg.adaptor.keycloak.gateway.KeycloakService
import org.koin.dsl.module

val keycloakKoins = module {
    single { KeycloakService(get()) }
}
