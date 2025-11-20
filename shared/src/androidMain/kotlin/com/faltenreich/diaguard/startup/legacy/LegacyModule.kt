package com.faltenreich.diaguard.startup.legacy

import com.faltenreich.diaguard.persistence.keyvalue.KEY_VALUE_STORE_LEGACY
import com.faltenreich.diaguard.startup.legacy.query.EntryLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.EntryTagLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.FoodEatenLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.FoodLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.KeyValueLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.MeasurementValueLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.TagLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.ActivityLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.BloodPressureLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.BloodSugarLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.HbA1cLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.InsulinLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.MealLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.OxygenSaturationLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.PulseLegacyQueries
import com.faltenreich.diaguard.startup.legacy.query.measurement.WeightLegacyQueries
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun legacyDaoModule() = module {
    factory { KeyValueLegacyQueries(keyValueStore = get(named(KEY_VALUE_STORE_LEGACY))) }

    factoryOf(::EntryLegacyQueries)

    factoryOf(::BloodSugarLegacyQueries)
    factoryOf(::InsulinLegacyQueries)
    factoryOf(::MealLegacyQueries)
    factoryOf(::ActivityLegacyQueries)
    factoryOf(::HbA1cLegacyQueries)
    factoryOf(::WeightLegacyQueries)
    factoryOf(::PulseLegacyQueries)
    factoryOf(::BloodPressureLegacyQueries)
    factoryOf(::OxygenSaturationLegacyQueries)

    factoryOf(::MeasurementValueLegacyQueries)
    factoryOf(::FoodLegacyQueries)
    factoryOf(::FoodEatenLegacyQueries)
    factoryOf(::TagLegacyQueries)
    factoryOf(::EntryTagLegacyQueries)

    factoryOf(::AndroidLegacyDao) bind LegacyDao::class
}