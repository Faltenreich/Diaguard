package com.faltenreich.diaguard.data.legacy.query

interface LegacyQueries<T> {

    fun getAll(): List<T>
}