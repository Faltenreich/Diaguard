package com.faltenreich.diaguard.startup.seed

import com.faltenreich.diaguard.TestSuite
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.data.seed.SeedRepository
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SeedRepositoryTest : TestSuite() {

    private val seedRepository: SeedRepository by inject()

    @Test
    fun `Units can be read`() {
        assertTrue(seedRepository.getUnits().isNotEmpty())
    }

    @Test
    fun `Units have unique keys`() {
        val units = seedRepository.getUnits()
        assertEquals(
            expected = units,
            actual = units.distinctBy(MeasurementUnit.Seed::key),
        )
    }

    @Test
    fun `Categories can be read`() {
        assertTrue(seedRepository.getCategories().isNotEmpty())
    }

    @Test
    fun `Categories have unique keys`() {
        val categories = seedRepository.getCategories()
        assertEquals(
            expected = categories,
            actual = categories.distinctBy(MeasurementCategory.Seed::key),
        )
    }

    @Test
    fun `Food can be read`() {
        assertTrue(seedRepository.getFood().isNotEmpty())
    }

    @Test
    fun `Tags can be read`() {
        assertTrue(seedRepository.getTags().isNotEmpty())
    }
}