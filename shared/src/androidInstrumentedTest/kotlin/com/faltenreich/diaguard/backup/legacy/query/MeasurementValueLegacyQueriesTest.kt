package com.faltenreich.diaguard.backup.legacy.query

import com.faltenreich.diaguard.backup.legacy.query.measurement.ActivityLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.BloodPressureLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.BloodSugarLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.HbA1cLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.InsulinLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.MealLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.OxygenSaturationLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.PulseLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.WeightLegacyQueries
import com.faltenreich.diaguard.datetime.kotlinx.KotlinxDateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.test.FileFactory
import org.junit.Assert
import org.junit.Test

class MeasurementValueLegacyQueriesTest {

    private val database = SqliteDatabase(file = FileFactory.createFromAssets("diaguard.db"))
    private val dateTimeFactory = KotlinxDateTimeFactory()

    private val queries = MeasurementValueLegacyQueries(
        bloodSugarQueries = BloodSugarLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        insulinQueries = InsulinLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        mealQueries = MealLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        activityQueries = ActivityLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        hba1cQueries = HbA1cLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        weightQueries = WeightLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        pulseQueries = PulseLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        BloodPressureLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
        OxygenSaturationLegacyQueries(
            database = database,
            dateTimeFactory = dateTimeFactory,
        ),
    )

    @Test
    fun readsMeasurements() {
        val expected = arrayOf(
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198203),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198203),
                value = 100.0,
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198206),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198206),
                value = 100.0,
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 3,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198208),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198208),
                value = 120.0,
                propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                entryId = 3,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198228),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198228),
                value = 1.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198228),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198228),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198228),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198228),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198246),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198246),
                value = 6.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198246),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198246),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198246),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198246),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198221),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198221),
                value = 0.0,
                propertyKey = DatabaseKey.MeasurementProperty.MEAL,
                entryId = 1,
            ),
            MeasurementValue.Legacy(
                id = 2,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198244),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198244),
                value = 60.0,
                propertyKey = DatabaseKey.MeasurementProperty.MEAL,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198232),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198232),
                value = 30.0,
                propertyKey = DatabaseKey.MeasurementProperty.ACTIVITY,
                entryId = 2,
            ),
            MeasurementValue.Legacy(
                id = 1,
                createdAt = dateTimeFactory.dateTime(millis = 1717865198241),
                updatedAt = dateTimeFactory.dateTime(millis = 1717865198241),
                value = 90.0,
                propertyKey = DatabaseKey.MeasurementProperty.PULSE,
                entryId = 3,
            ),
        )
        val actual = queries.getAll().toTypedArray()
        Assert.assertArrayEquals(expected, actual)
    }
}