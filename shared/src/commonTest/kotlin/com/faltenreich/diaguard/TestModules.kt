package com.faltenreich.diaguard

import com.faltenreich.diaguard.backup.legacy.LegacyDao
import com.faltenreich.diaguard.backup.legacy.LegacyFakeDao
import com.faltenreich.diaguard.backup.seed.query.food.FoodSeedQueries
import com.faltenreich.diaguard.backup.seed.query.tag.TagSeedQueries
import com.faltenreich.diaguard.food.api.FoodApi
import com.faltenreich.diaguard.food.api.openfoodfacts.OpenFoodFactsApi
import com.faltenreich.diaguard.measurement.value.StoreMeasurementValueUseCase
import com.faltenreich.diaguard.shared.database.sqldelight.sqlDelightModule
import com.faltenreich.diaguard.shared.file.SystemFileReader
import com.faltenreich.diaguard.shared.keyvalue.FakeKeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.localization.FakeLocalization
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.logging.ConsoleLogger
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.shared.serialization.Serialization
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun testModules() = module {
    includes(sqlDelightModule(inMemory = true))

    single<CoroutineDispatcher> { StandardTestDispatcher() }
    single<CoroutineContext> { StandardTestDispatcher() }
    single<CoroutineScope> { TestScope(context = get()) }

    single<Logger> { ConsoleLogger() }

    single<KeyValueStore> { FakeKeyValueStore() }

    single<FoodApi> {
        OpenFoodFactsApi(
            client = { SystemFileReader("src/commonTest/resources/networking/openfoodfacts.json").read() },
        )
    }

    single<LegacyDao> { LegacyFakeDao() }

    single<Localization> { FakeLocalization() }

    single {
        FoodSeedQueries(
            fileReader = SystemFileReader("src/commonTest/resources/seed/food.csv"),
            serialization = Serialization(),
            localization = FakeLocalization(),
        )
    }
    single {
        TagSeedQueries(
            fileReader = SystemFileReader("src/commonTest/resources/seed/tags.csv"),
            serialization = Serialization(),
            localization = FakeLocalization(),
        )
    }

    singleOf(::StoreMeasurementValueUseCase)
}