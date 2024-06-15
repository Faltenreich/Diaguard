package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.query.EntryLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.EntryTagLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.FoodEatenLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.FoodLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.MeasurementValueLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.TagLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.ActivityLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.BloodPressureLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.BloodSugarLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.HbA1cLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.InsulinLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.MealLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.OxygenSaturationLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.PulseLegacyQueries
import com.faltenreich.diaguard.backup.legacy.query.measurement.WeightLegacyQueries
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase

object LegacySqliteDaoFactory {

    fun createDao(
        database: SqliteDatabase,
        dateTimeFactory: DateTimeFactory,
    ): LegacySqliteDao {
        return LegacySqliteDao(
            entryQueries = EntryLegacyQueries(
                database = database,
                dateTimeFactory = dateTimeFactory,
            ),
            measurementValueQueries = MeasurementValueLegacyQueries(
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
            ),
            foodQueries = FoodLegacyQueries(
                database = database,
                dateTimeFactory = dateTimeFactory,
            ),
            foodEatenQueries = FoodEatenLegacyQueries(
                database = database,
                dateTimeFactory = dateTimeFactory,
            ),
            tagQueries = TagLegacyQueries(
                database = database,
                dateTimeFactory = dateTimeFactory,
            ),
            entryTagQueries = EntryTagLegacyQueries(
                database = database,
                dateTimeFactory = dateTimeFactory,
            ),
        )
    }
}