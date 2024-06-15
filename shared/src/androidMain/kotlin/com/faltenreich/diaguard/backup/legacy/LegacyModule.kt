package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.legacy.dao.LegacyEntrySqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyEntryTagSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyFoodEatenSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyFoodSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyMeasurementValueSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.LegacyTagSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyActivitySqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyBloodPressureSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyBloodSugarSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyHbA1cSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyInsulinSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyMealSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyOxygenSaturationSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyPulseSqliteDao
import com.faltenreich.diaguard.backup.legacy.dao.measurement.LegacyWeightSqliteDao
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