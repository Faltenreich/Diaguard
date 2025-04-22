package com.faltenreich.diaguard.startup.seed.query

interface SeedQueries<T> {

    fun getAll(): List<T>
}