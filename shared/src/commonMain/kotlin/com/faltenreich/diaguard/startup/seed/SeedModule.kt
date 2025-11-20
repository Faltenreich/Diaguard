package com.faltenreich.diaguard.startup.seed

import com.faltenreich.diaguard.persistence.file.ResourceFileReader
import com.faltenreich.diaguard.startup.seed.query.food.FoodSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.ActivitySeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.BloodPressureSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.BloodSugarSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.HbA1cSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.InsulinSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.MealSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.MeasurementUnitSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.OxygenSaturationSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.PulseSeedQueries
import com.faltenreich.diaguard.startup.seed.query.measurement.WeightSeedQueries
import com.faltenreich.diaguard.startup.seed.query.tag.TagSeedQueries
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun seedModule() = module {
    factoryOf(::BloodSugarSeedQueries)
    factoryOf(::InsulinSeedQueries)
    factoryOf(::MealSeedQueries)
    factoryOf(::ActivitySeedQueries)
    factoryOf(::HbA1cSeedQueries)
    factoryOf(::WeightSeedQueries)
    factoryOf(::PulseSeedQueries)
    factoryOf(::BloodPressureSeedQueries)
    factoryOf(::OxygenSaturationSeedQueries)

    factoryOf(::MeasurementUnitSeedQueries)
    factoryOf(::MeasurementCategorySeedQueries)

    factory {
        FoodSeedQueries(
            fileReader = ResourceFileReader("files/food_common.csv"),
            serialization = get(),
            localization = get(),
        )
    }

    factory {
        TagSeedQueries(
            fileReader = ResourceFileReader("files/tags.csv"),
            serialization = get(),
            localization = get(),
        )
    }

    factory<SeedDao> {
        SeedBundleDao(
            unitQueries = get(),
            categoryQueries = get(),
            foodQueries = get(),
            tagQueries = get(),
        )
    }

    factoryOf(::SeedRepository)

    factoryOf(::ImportSeedUseCase)
}