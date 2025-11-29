package com.faltenreich.diaguard.startup.legacy.query

interface LegacyQueries<T> {

    fun getAll(): List<T>
}