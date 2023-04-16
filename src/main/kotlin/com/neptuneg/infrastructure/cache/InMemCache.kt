package com.neptuneg.infrastructure.cache

class InMemCache<K, V>(
    private val cache: MutableMap<K, V> = mutableMapOf()
) {
    fun fetch(key: K, block: () -> V): V = cache[key] ?: block().apply { cache[key] = this }

    operator fun set(key: K, value: V): Unit = run { cache[key] = value }

    operator fun get(key: K): V? = cache[key]

    companion object {
        fun <K, V> emptyCache() = InMemCache<K, V>()
    }
}
