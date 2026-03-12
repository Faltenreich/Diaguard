package com.faltenreich.diaguard.data.seed.query

interface SeedQueries<T> {

    fun getAll(): List<T>
}