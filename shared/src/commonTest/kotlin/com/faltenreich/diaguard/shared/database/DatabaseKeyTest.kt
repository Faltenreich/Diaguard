package com.faltenreich.diaguard.shared.database

import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseKeyTest {

    @Test
    fun `Database keys are unique`() {
        assertEquals(
            expected = DatabaseKey.MeasurementCategory.entries,
            actual = DatabaseKey.MeasurementCategory.entries.distinctBy(DatabaseKey::key)
        )
        assertEquals(
            expected = DatabaseKey.MeasurementProperty.entries,
            actual = DatabaseKey.MeasurementProperty.entries.distinctBy(DatabaseKey::key)
        )
        assertEquals(
            expected = DatabaseKey.MeasurementUnit.entries,
            actual = DatabaseKey.MeasurementUnit.entries.distinctBy(DatabaseKey::key)
        )
    }
}