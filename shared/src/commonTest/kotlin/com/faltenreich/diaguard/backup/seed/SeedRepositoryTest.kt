package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SeedRepositoryTest : TestSuite {

    private val seedRepository: SeedRepository by inject()

    @Test
    fun `Seed categories can be read`() {
        assertTrue(seedRepository.getMeasurementCategories().isNotEmpty())
    }

    @Test
    fun `Seed categories have unique keys`() {
        val categories = seedRepository.getMeasurementCategories()
        assertEquals(
            expected = categories,
            actual = categories.distinctBy(MeasurementCategory.Seed::key),
        )
    }

    @Test
    fun `Seed food can be read`() {
        assertTrue(seedRepository.getFood().isNotEmpty())
    }

    @Test
    fun `Seed tags can be read`() {
        assertTrue(seedRepository.getTags().isNotEmpty())
    }
}