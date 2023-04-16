package com.neptuneg.infrastructure

import io.kotest.core.spec.style.FunSpec

@Suppress("UnnecessaryAbstractClass")
abstract class RepositorySpec(body: FunSpec.() -> Unit = {}) : FunSpec(body) {
    init {
        beforeSpec { TestDatabase.connect() }
        afterSpec { TestDatabase.clear() }
    }
}
