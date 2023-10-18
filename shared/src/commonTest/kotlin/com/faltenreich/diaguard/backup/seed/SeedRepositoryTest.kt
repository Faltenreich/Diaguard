package com.faltenreich.diaguard.backup.seed

import kotlin.test.Test
import kotlin.test.assertFalse

class SeedRepositoryTest {

    private val seedRepository = SeedRepository()

    @Test
    fun `Seeded properties and its types and units have unique keys`() {
        val propertyKeys = mutableListOf<String>()
        val typeKeys = mutableListOf<String>()
        val unitKeys = mutableListOf<String>()

        val seed = seedRepository.seedMeasurementProperties()
        val properties = seed.map(Seed<SeedMeasurementProperty>::harvest)
        properties.forEach { property ->
            val propertyKey = property.key
            assertFalse(propertyKey in propertyKeys)
            propertyKeys += propertyKey

            property.types.forEach { type ->
                val typeKey = type.key
                assertFalse(typeKey in typeKeys)
                typeKeys += typeKey

                type.units.forEach { unit ->
                    val unitKey = unit.key
                    assertFalse(unitKey in unitKeys)
                    unitKeys += unitKey
                }
            }
        }
    }
}