package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.query.FoodSeed
import com.faltenreich.diaguard.backup.seed.query.TagSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.ActivitySeed
import com.faltenreich.diaguard.backup.seed.query.measurement.BloodPressureSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.BloodSugarSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.HbA1cSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.InsulinSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.MealSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.OxygenSaturationSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.PulseSeed
import com.faltenreich.diaguard.backup.seed.query.measurement.WeightSeed
import com.faltenreich.diaguard.shared.file.ResourceFileReader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun seedModule() = module {
    singleOf(::BloodSugarSeed)
    singleOf(::InsulinSeed)
    singleOf(::MealSeed)
    singleOf(::ActivitySeed)
    singleOf(::HbA1cSeed)
    singleOf(::WeightSeed)
    singleOf(::PulseSeed)
    singleOf(::BloodPressureSeed)
    singleOf(::OxygenSaturationSeed)
    single { FoodSeed(fileReader = ResourceFileReader("files/food_common.csv"), serialization = get()) }
    single { TagSeed(fileReader = ResourceFileReader("files/tags.csv"), serialization = get()) }

    singleOf(::SeedRepository)

    singleOf(::SeedImport)
}