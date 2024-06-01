package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.DependencyInjection
import com.faltenreich.diaguard.appModules
import com.faltenreich.diaguard.testModules
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class LegacyRepositoryTest : KoinTest {

    private val repository: LegacyRepository by inject()

    @BeforeTest
    fun setup() {
        DependencyInjection.setup(modules = appModules() + testModules())
    }

    @Test
    fun `returns entries`() {
        assertNotNull(repository.getEntries())
    }

    @Test
    fun `returns values`() {
        assertNotNull(repository.getMeasurementValues())
    }

    @Test
    fun `returns tags`() {
        assertNotNull(repository.getTags())
    }
}