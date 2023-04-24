package com.faltenreich.diaguard.entry

import kotlinx.coroutines.flow.Flow

interface EntryDao {

    fun getAll(): Flow<List<Entry>>

    fun insert(entry: Entry)

    fun delete(entry: Entry)
}