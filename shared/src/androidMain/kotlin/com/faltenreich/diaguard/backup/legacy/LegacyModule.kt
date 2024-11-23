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
import com.faltenreich.diaguard.shared.keyvalue.KEY_VALUE_STORE_LEGACY
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun legacyDaoModule() = module {
    singleOf(::EntryLegacyQueries)

    singleOf(::BloodSugarLegacyQueries)
    singleOf(::InsulinLegacyQueries)
    singleOf(::MealLegacyQueries)
    singleOf(::ActivityLegacyQueries)
    singleOf(::HbA1cLegacyQueries)
    singleOf(::WeightLegacyQueries)
    singleOf(::PulseLegacyQueries)
    singleOf(::BloodPressureLegacyQueries)
    singleOf(::OxygenSaturationLegacyQueries)

    singleOf(::MeasurementValueLegacyQueries)

    singleOf(::FoodLegacyQueries)

    singleOf(::FoodEatenLegacyQueries)

    singleOf(::TagLegacyQueries)

    singleOf(::EntryTagLegacyQueries)

    single<LegacyDao> {
        AndroidLegacyDao(
            keyValueStore = get(named(KEY_VALUE_STORE_LEGACY)),
            entryQueries = get(),
            measurementValueQueries = get(),
            foodQueries = get(),
            foodEatenQueries = get(),
            tagQueries = get(),
            entryTagQueries = get(),
        )
    }
}