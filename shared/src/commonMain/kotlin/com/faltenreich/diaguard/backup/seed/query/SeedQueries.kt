package com.faltenreich.diaguard.backup.seed.query

interface SeedQueries<T> {

    fun getAll(): List<T>
}