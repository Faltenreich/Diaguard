package com.faltenreich.diaguard

import com.faltenreich.diaguard.backup.legacy.LegacyDao
import com.faltenreich.diaguard.backup.legacy.LegacyFakeDao
import com.faltenreich.diaguard.backup.seed.query.FoodSeed
import com.faltenreich.diaguard.backup.seed.query.TagSeed
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.entry.EntryFakeDao
import com.faltenreich.diaguard.entry.tag.EntryTagDao
import com.faltenreich.diaguard.entry.tag.EntryTagFakeDao
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.FoodFakeDao
import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsApi
import com.faltenreich.diaguard.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.food.eaten.FoodEatenFakeDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryFakeDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyFakeDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitFakeDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueFakeDao
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.localization.FakeLocalization
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.logging.ConsoleLogger
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.tag.TagDao
import com.faltenreich.diaguard.tag.TagFakeDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun testModules() = module {
    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<CoroutineContext> { StandardTestDispatcher() }
    single<CoroutineScope> { TestScope(context = get()) }

    single<Logger> { ConsoleLogger() }

    single<FoodApi> {
        OpenFoodFactsApi(
            client = { SystemFileReader("src/commonTest/resources/networking/openfoodfacts.json").read() },
        )
    }

    single<EntryDao> { EntryFakeDao() }
    single<MeasurementCategoryDao> { MeasurementCategoryFakeDao() }
    single<MeasurementPropertyDao> { MeasurementPropertyFakeDao() }
    single<MeasurementUnitDao> { MeasurementUnitFakeDao() }
    single<MeasurementValueDao> { MeasurementValueFakeDao() }
    single<FoodDao> { FoodFakeDao() }
    single<FoodEatenDao> { FoodEatenFakeDao() }
    single<TagDao> { TagFakeDao() }
    single<EntryTagDao> { EntryTagFakeDao() }

    single<LegacyDao> { LegacyFakeDao() }

    single<Localization> { FakeLocalization() }

    single {
        FoodSeed(
            fileReader = SystemFileReader("src/commonTest/resources/seed/food.csv"),
            serialization = Serialization(),
        )
    }
    single {
        TagSeed(
            fileReader = SystemFileReader("src/commonTest/resources/seed/tags.csv"),
            serialization = Serialization(),
        )
    }
}