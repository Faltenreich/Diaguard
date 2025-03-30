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

    @Test
    fun `Database keys are valid`() {
        // MeasurementUnit covers MeasurementProperty and MeasurementCategory
        assertEquals(
            expected = "blood_sugar.blood_sugar.milligrams_per_deciliter",
            actual = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIGRAMS_PER_DECILITER.key,
        )
        assertEquals(
            expected = "blood_sugar.blood_sugar.millimoles_per_liter",
            actual = DatabaseKey.MeasurementUnit.BLOOD_SUGAR_MILLIMOLES_PER_LITER.key,
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
        assertEquals(
            expected = "activity.activity.minutes",
            actual = DatabaseKey.MeasurementUnit.ACTIVITY.key,
        )
        assertEquals(
            expected = "hba1c.hba1c.percent",
            actual = DatabaseKey.MeasurementUnit.HBA1C_PERCENT.key,
        )
        assertEquals(
            expected = "hba1c.hba1c.millimoles_per_mole",
            actual = DatabaseKey.MeasurementUnit.HBA1C_MILLIMOLES_PER_MOLES.key,
        )
        assertEquals(
            expected = "weight.weight.kilograms",
            actual = DatabaseKey.MeasurementUnit.WEIGHT_KILOGRAMS.key,
        )
        assertEquals(
            expected = "weight.weight.pounds",
            actual = DatabaseKey.MeasurementUnit.WEIGHT_POUNDS.key,
        )
        assertEquals(
            expected = "pulse.pulse.beats_per_minute",
            actual = DatabaseKey.MeasurementUnit.PULSE.key,
        )
        assertEquals(
            expected = "blood_pressure.systolic.millimeters_of_mercury",
            actual = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_SYSTOLIC.key,
        )
        assertEquals(
            expected = "blood_pressure.diastolic.millimeters_of_mercury",
            actual = DatabaseKey.MeasurementUnit.BLOOD_PRESSURE_DIASTOLIC.key,
        )
        assertEquals(
            expected = "oxygen_saturation.oxygen_saturation.percent",
            actual = DatabaseKey.MeasurementUnit.OXYGEN_SATURATION.key,
        )
    }
}