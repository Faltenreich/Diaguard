package com.faltenreich.diaguard.tag

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class TagDaoFake : TagDao {

    override fun create(createdAt: DateTime, updatedAt: DateTime, name: String) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Tag.Local? {
        TODO("Not yet implemented")
    }

    override fun observeAll(): Flow<List<Tag.Local>> {
        TODO("Not yet implemented")
    }

    override fun observeByQuery(query: String): Flow<List<Tag.Local>> {
        TODO("Not yet implemented")
    }

    override fun getByName(name: String): Tag.Local? {
        TODO("Not yet implemented")
    }

    override fun update(id: Long, updatedAt: DateTime, name: String) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}