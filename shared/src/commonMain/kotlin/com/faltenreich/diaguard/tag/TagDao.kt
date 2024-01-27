package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.shared.datetime.DateTime
import kotlinx.coroutines.flow.Flow

interface TagDao {

    fun create(
        createdAt: DateTime,
        updatedAt: DateTime,
        name: String,
    )

    fun getLastId(): Long?

    fun getById(id: Long): Tag?

    fun observeAll(): Flow<List<Tag>>

    fun observeByQuery(query: String): Flow<List<Tag>>

    fun getByName(name: String): Tag?

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    )

    fun deleteById(id: Long)
}