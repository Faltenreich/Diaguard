package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface TagDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        name: String,
    )

    fun getLastId(): Long?

    fun getById(id: Long): Tag.Persistent?

    fun observeAll(): Flow<List<Tag.Persistent>>

    fun observeByQuery(query: String): Flow<List<Tag.Persistent>>

    fun getByName(name: String): Tag.Persistent?

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    )

    fun deleteById(id: Long)
}