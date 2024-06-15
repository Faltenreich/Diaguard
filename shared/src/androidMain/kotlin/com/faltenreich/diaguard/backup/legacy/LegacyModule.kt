package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.measurement.LegacyActivitySqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyBloodPressureSqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyBloodSugarSqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyHbA1cSqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyInsulinSqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyMealSqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyOxygenSaturationSqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyPulseSqliteDao
import com.faltenreich.diaguard.backup.legacy.measurement.LegacyWeightSqliteDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun legacyDaoModule() = module {
    singleOf(::LegacyEntrySqliteDao)

    singleOf(::LegacyBloodSugarSqliteDao)
    singleOf(::LegacyInsulinSqliteDao)
    singleOf(::LegacyMealSqliteDao)
    singleOf(::LegacyActivitySqliteDao)
    singleOf(::LegacyHbA1cSqliteDao)
    singleOf(::LegacyWeightSqliteDao)
    singleOf(::LegacyPulseSqliteDao)
    singleOf(::LegacyBloodPressureSqliteDao)
    singleOf(::LegacyOxygenSaturationSqliteDao)

    singleOf(::LegacyMeasurementValueSqliteDao)

    singleOf(::LegacyFoodSqliteDao)

    singleOf(::LegacyFoodEatenSqliteDao)

    singleOf(::LegacyTagSqliteDao)

    singleOf(::LegacyEntryTagSqliteDao)

    single<LegacyDao> {
        LegacySqliteDao(
            entryDao = get(),
            measurementValueDao = get(),
            foodDao = get(),
            foodEatenDao = get(),
            tagDao = get(),
            entryTagDao = get(),
        )
    }
}