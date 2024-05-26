package com.faltenreich.diaguard.entry.tag

import com.faltenreich.diaguard.datetime.DateTime
import kotlinx.coroutines.flow.Flow

class EntryTagDaoFake : EntryTagDao {

    override fun create(createdAt: DateTime, updatedAt: DateTime, entryId: Long, tagId: Long) {
        TODO("Not yet implemented")
    }

    override fun getLastId(): Long? {
        TODO("Not yet implemented")
    }

    override fun getByEntryId(entryId: Long): List<EntryTag.Local> {
        TODO("Not yet implemented")
    }

    override fun observeByTagId(tagId: Long): Flow<List<EntryTag.Local>> {
        TODO("Not yet implemented")
    }

    override fun countByTagId(tagId: Long): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }
}