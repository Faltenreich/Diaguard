package com.faltenreich.diaguard.shared.database

import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseKeyTest {

    @Test
    fun `Keys are valid for blood sugar`() {
        assertEquals(
            expected = "blood_sugar",
            actual = DatabaseKey.MeasurementProperty.BLOOD_SUGAR.key,
        )
        assertEquals(
            expected = "blood_sugar.blood_sugar",
            actual = DatabaseKey.MeasurementType.BLOOD_SUGAR.key,
        )
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
    fun `Keys are valid for insulin`() {
        assertEquals(
            expected = "insulin",
            actual = DatabaseKey.MeasurementProperty.INSULIN.key,
        )
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
        assertEquals(
            expected = "insulin.bolus.insulin_units",
            actual = DatabaseKey.MeasurementUnit.INSULIN_BOLUS.key,
        )
        assertEquals(
            expected = "insulin.correction.insulin_units",
            actual = DatabaseKey.MeasurementUnit.INSULIN_CORRECTION.key,
        )
        assertEquals(
            expected = "insulin.basal.insulin_units",
            actual = DatabaseKey.MeasurementUnit.INSULIN_BASAL.key,
        )
    }

    @Test
    fun `Keys are valid for meal`() {
        assertEquals(
            expected = "meal",
            actual = DatabaseKey.MeasurementProperty.MEAL.key,
        )
        assertEquals(
            expected = "meal.meal",
            actual = DatabaseKey.MeasurementType.MEAL.key,
        )
        assertEquals(
            expected = "meal.meal.carbohydrates",
            actual = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATES.key,
        )
        assertEquals(
            expected = "meal.meal.carbohydrate_units",
            actual = DatabaseKey.MeasurementUnit.MEAL_CARBOHYDRATE_UNITS.key,
        )
        assertEquals(
            expected = "meal.meal.bread_units",
            actual = DatabaseKey.MeasurementUnit.MEAL_BREAD_UNITS.key,
        )
    }

    // TODO: Other keys
}