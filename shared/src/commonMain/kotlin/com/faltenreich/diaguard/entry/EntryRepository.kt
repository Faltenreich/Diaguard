package com.faltenreich.diaguard.entry

import org.koin.core.annotation.Single

@Single
class EntryRepository(private val dao: EntryDao) {

}