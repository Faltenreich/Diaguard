package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.seed.data.ActivitySeed
import com.faltenreich.diaguard.backup.seed.data.BloodPressureSeed
import com.faltenreich.diaguard.backup.seed.data.BloodSugarSeed
import com.faltenreich.diaguard.backup.seed.data.FoodSeed
import com.faltenreich.diaguard.backup.seed.data.HbA1cSeed
import com.faltenreich.diaguard.backup.seed.data.InsulinSeed
import com.faltenreich.diaguard.backup.seed.data.MealSeed
import com.faltenreich.diaguard.backup.seed.data.OxygenSaturationSeed
import com.faltenreich.diaguard.backup.seed.data.PulseSeed
import com.faltenreich.diaguard.backup.seed.data.TagSeed
import com.faltenreich.diaguard.backup.seed.data.WeightSeed
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