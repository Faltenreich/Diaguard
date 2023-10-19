package com.faltenreich.diaguard.shared.database

import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseKeyTest {

    @Test
    fun `Key is valid for property of blood sugar`() {
        assertEquals(
            expected = "blood_sugar",
            actual = DatabaseKey.MeasurementProperty.BLOOD_SUGAR.key,
        )
    }

    @Test
    fun `Key is valid for types of blood sugar`() {
        assertEquals(
            expected = "blood_sugar.blood_sugar",
            actual = DatabaseKey.MeasurementType.BLOOD_SUGAR.key,
        )
    }

    @Test
    fun `Key is valid for units of blood sugar`() {
        assertEquals(
            expected = "blood_sugar.blood_sugar.milligrams_per_deciliter",
            actual = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER.key,
        )
        assertEquals(
            expected = "blood_sugar.blood_sugar.millimoles_per_liter",
            actual = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIMOLES_PER_LITER.key,
        )
    }

    @Test
    fun `Key is valid for property of insulin`() {
        assertEquals(
            expected = "insulin",
            actual = DatabaseKey.MeasurementProperty.INSULIN.key,
        )
    }

    @Test
    fun `Key is valid for types of insulin`() {
        assertEquals(
            expected = "insulin.bolus",
            actual = DatabaseKey.MeasurementType.INSULIN_BOLUS.key,
        )
        assertEquals(
            expected = "insulin.correction",
            actual = DatabaseKey.MeasurementType.INSULIN_CORRECTION.key,
        )
        assertEquals(
            expected = "insulin.basal",
            actual = DatabaseKey.MeasurementType.INSULIN_BASAL.key,
        )
    }

    @Test
    fun `Key is valid for units of insulin`() {
        assertEquals(
            expected = "insulin.bolus.insulin_units",
            actual = DatabaseKey.MeasurementUnit.INSULIN_BOLUS_INSULIN_UNITS.key,
        )
        assertEquals(
            expected = "insulin.correction.insulin_units",
            actual = DatabaseKey.MeasurementUnit.INSULIN_CORRECTION_INSULIN_UNITS.key,
        )
        assertEquals(
            expected = "insulin.basal.insulin_units",
            actual = DatabaseKey.MeasurementUnit.INSULIN_BASAL_INSULIN_UNITS.key,
        )
    }
}