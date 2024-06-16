package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.query.food.FoodSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.backup.seed.query.tag.TagSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.ActivitySeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.BloodPressureSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.BloodSugarSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.HbA1cSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.InsulinSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.MealSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.OxygenSaturationSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.PulseSeedQueries
import com.faltenreich.diaguard.backup.seed.query.measurement.WeightSeedQueries
import com.faltenreich.diaguard.shared.file.ResourceFileReader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun seedModule() = module {
    singleOf(::BloodSugarSeedQueries)
    singleOf(::InsulinSeedQueries)
    singleOf(::MealSeedQueries)
    singleOf(::ActivitySeedQueries)
    singleOf(::HbA1cSeedQueries)
    singleOf(::WeightSeedQueries)
    singleOf(::PulseSeedQueries)
    singleOf(::BloodPressureSeedQueries)
    singleOf(::OxygenSaturationSeedQueries)

    singleOf(::MeasurementCategorySeedQueries)

    single { FoodSeedQueries(fileReader = ResourceFileReader("files/food_common.csv"), serialization = get()) }

    single { TagSeedQueries(fileReader = ResourceFileReader("files/tags.csv"), serialization = get()) }

    single<SeedDao> {
        SeedBundleDao(
            measurementCategoryQueries = get(),
            foodQueries = get(),
            tagQueries = get(),
        )
    }

    singleOf(::SeedRepository)

    singleOf(::SeedImport)
}