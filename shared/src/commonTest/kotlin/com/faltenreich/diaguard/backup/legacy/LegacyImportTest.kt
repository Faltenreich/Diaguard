package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.entry.EntryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertTrue

class LegacyImportTest : TestSuite {

    private val importLegacy: LegacyImportUseCase by inject()
    private val entryRepository: EntryRepository by inject()

    @Test
    fun `imports entries`() = runTest {
        assertTrue(entryRepository.getAll().first().isEmpty())

        importSeed()
        importLegacy()

        assertTrue(entryRepository.getAll().first().isNotEmpty())
    }
}