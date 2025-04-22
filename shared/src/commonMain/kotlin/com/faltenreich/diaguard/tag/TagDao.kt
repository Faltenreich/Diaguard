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

    fun getById(id: Long): Tag.Local?

    fun observeAll(): Flow<List<Tag.Local>>

    fun observeByQuery(query: String): Flow<List<Tag.Local>>

    fun getByName(name: String): Tag.Local?

    fun update(
        id: Long,
        updatedAt: DateTime,
        name: String,
    )

    fun deleteById(id: Long)
}