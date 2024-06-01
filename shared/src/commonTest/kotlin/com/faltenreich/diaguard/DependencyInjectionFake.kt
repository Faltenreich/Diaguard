package com.faltenreich.diaguard

import com.faltenreich.diaguard.backup.legacy.LegacyDao
import com.faltenreich.diaguard.backup.legacy.LegacyDaoFake
import com.faltenreich.diaguard.backup.seed.data.FoodSeed
import com.faltenreich.diaguard.backup.seed.data.TagSeed
import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.entry.EntryDaoFake
import com.faltenreich.diaguard.entry.tag.EntryTagDao
import com.faltenreich.diaguard.entry.tag.EntryTagDaoFake
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.FoodDaoFake
import com.faltenreich.diaguard.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.food.eaten.FoodEatenDaoFake
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDaoFake
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDaoFake
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDaoFake
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueDaoFake
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.localization.ResourceLocalization
import com.faltenreich.diaguard.shared.localization.ResourceLocalizationFake
import com.faltenreich.diaguard.shared.logging.ConsoleLogger
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.serialization.Serialization
import com.faltenreich.diaguard.tag.TagDao
import com.faltenreich.diaguard.tag.TagDaoFake
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

    single<EntryDao> { EntryDaoFake() }
    single<MeasurementCategoryDao> { MeasurementCategoryDaoFake() }
    single<MeasurementPropertyDao> { MeasurementPropertyDaoFake() }
    single<MeasurementUnitDao> { MeasurementUnitDaoFake() }
    single<MeasurementValueDao> { MeasurementValueDaoFake() }
    single<FoodDao> { FoodDaoFake() }
    single<FoodEatenDao> { FoodEatenDaoFake() }
    single<TagDao> { TagDaoFake() }
    single<EntryTagDao> { EntryTagDaoFake() }

    single<LegacyDao> { LegacyDaoFake() }

    single<ResourceLocalization> { ResourceLocalizationFake() }

    single {
        FoodSeed(
            fileReader = SystemFileReader("src/commonTest/resources/food.csv"),
            serialization = Serialization(),
        )
    }
    single {
        TagSeed(
            fileReader = SystemFileReader("src/commonTest/resources/tags.csv"),
            serialization = Serialization(),
        )
    }
}