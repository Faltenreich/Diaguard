package com.faltenreich.diaguard.backup.legacy.query

interface LegacyQueries<T> {

    fun getAll(): List<T>
}