package com.faltenreich.diaguard.backup.seed

import kotlin.test.Test
import kotlin.test.assertEquals

class SeedRepositoryTest {

    private val seedRepository = SeedRepository()

    @Test
    fun `Seeded properties have unique keys`() {
        val seed = seedRepository.seedMeasurementProperties()
        val properties = seed.map(Seed<SeedMeasurementProperty>::harvest)
        assertEquals(
            expected = properties,
            actual = properties.distinctBy(SeedMeasurementProperty::key),
        )
    }
}