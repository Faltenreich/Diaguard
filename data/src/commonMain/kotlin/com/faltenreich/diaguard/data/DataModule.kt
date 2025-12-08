package com.faltenreich.diaguard.data

import com.faltenreich.diaguard.architecture.architectureModule
import com.faltenreich.diaguard.config.configModule
import com.faltenreich.diaguard.data.entry.EntryDao
import com.faltenreich.diaguard.data.entry.EntryRepository
import com.faltenreich.diaguard.data.entry.EntrySqlDelightDao
import com.faltenreich.diaguard.data.entry.EntrySqlDelightMapper
import com.faltenreich.diaguard.data.entry.tag.EntryTagDao
import com.faltenreich.diaguard.data.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.data.entry.tag.EntryTagSqlDelightDao
import com.faltenreich.diaguard.data.entry.tag.EntryTagSqlDelightMapper
import com.faltenreich.diaguard.data.food.FoodDao
import com.faltenreich.diaguard.data.food.FoodRepository
import com.faltenreich.diaguard.data.food.FoodSqlDelightDao
import com.faltenreich.diaguard.data.food.FoodSqlDelightMapper
import com.faltenreich.diaguard.data.food.api.FoodApi
import com.faltenreich.diaguard.data.food.api.openfoodfacts.OpenFoodFactsApi
import com.faltenreich.diaguard.data.food.api.openfoodfacts.OpenFoodFactsMapper
import com.faltenreich.diaguard.data.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.data.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.data.food.eaten.FoodEatenSqlDelightDao
import com.faltenreich.diaguard.data.food.eaten.FoodEatenSqlDelightMapper
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategorySqlDelightDao
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategorySqlDelightMapper
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertySqlDelightDao
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertySqlDelightMapper
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitSqlDelightDao
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitSqlDelightMapper
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionDao
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionRepository
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionSqlDelightDao
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionSqlDelightMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueSqlDelightDao
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueSqlDelightMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueTintMapper
import com.faltenreich.diaguard.data.navigation.Navigation
import com.faltenreich.diaguard.data.preference.PreferenceDao
import com.faltenreich.diaguard.data.preference.PreferenceRepository
import com.faltenreich.diaguard.data.seed.SeedBundleDao
import com.faltenreich.diaguard.data.seed.SeedDao
import com.faltenreich.diaguard.data.seed.SeedRepository
import com.faltenreich.diaguard.data.seed.query.food.FoodSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.ActivitySeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.BloodPressureSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.BloodSugarSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.HbA1cSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.InsulinSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.MealSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.MeasurementCategorySeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.MeasurementUnitSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.OxygenSaturationSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.PulseSeedQueries
import com.faltenreich.diaguard.data.seed.query.measurement.WeightSeedQueries
import com.faltenreich.diaguard.data.seed.query.tag.TagSeedQueries
import com.faltenreich.diaguard.data.tag.TagDao
import com.faltenreich.diaguard.data.tag.TagRepository
import com.faltenreich.diaguard.data.tag.TagSqlDelightDao
import com.faltenreich.diaguard.data.tag.TagSqlDelightMapper
import com.faltenreich.diaguard.datetime.dateTimeModule
import com.faltenreich.diaguard.localization.localizationModule
import com.faltenreich.diaguard.logging.loggingModule
import com.faltenreich.diaguard.network.networkModule
import com.faltenreich.diaguard.persistence.database.SqlDelightDriverFactory
import com.faltenreich.diaguard.persistence.file.ResourceFileReader
import com.faltenreich.diaguard.persistence.persistenceModule
import com.faltenreich.diaguard.serialization.serializationModule
import com.faltenreich.diaguard.system.systemModule
import com.faltenreich.diaguard.view.viewModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    includes(
        architectureModule(),
        configModule(),
        dateTimeModule(),
        localizationModule(),
        loggingModule(),
        networkModule(),
        persistenceModule(inMemory = false),
        serializationModule(),
        systemModule(),
        viewModule(),
    )
    includes(dataPlatformModule())

    // Requires Singleton to support synchronous CRUD operations
    single<SqlDelightApi> {
        val driverFactory = get<SqlDelightDriverFactory>()
        val schema = SqlDelightApi.Schema
        val driver = driverFactory.createDriver(schema)
        SqlDelightApi(driver)
    }

    factory<EntryQueries> { get<SqlDelightApi>().entryQueries }
    factoryOf(::EntrySqlDelightDao) bind EntryDao::class
    factoryOf(::EntrySqlDelightMapper)
    factoryOf(::EntryRepository)

    factory<MeasurementCategoryQueries> { get<SqlDelightApi>().measurementCategoryQueries }
    factoryOf(::MeasurementCategorySqlDelightDao) bind MeasurementCategoryDao::class
    factoryOf(::MeasurementCategorySqlDelightMapper)
    factoryOf(::MeasurementCategoryRepository)

    factory<MeasurementPropertyQueries> { get<SqlDelightApi>().measurementPropertyQueries }
    factoryOf(::MeasurementPropertySqlDelightDao) bind MeasurementPropertyDao::class
    factoryOf(::MeasurementPropertySqlDelightMapper)
    factoryOf(::MeasurementPropertyRepository)

    factory<MeasurementUnitQueries> { get<SqlDelightApi>().measurementUnitQueries }
    factoryOf(::MeasurementUnitSqlDelightDao) bind MeasurementUnitDao::class
    factoryOf(::MeasurementUnitSqlDelightMapper)
    factoryOf(::MeasurementUnitRepository)

    factory<MeasurementUnitSuggestionQueries> { get<SqlDelightApi>().measurementUnitSuggestionQueries }
    factoryOf(::MeasurementUnitSuggestionSqlDelightDao) bind MeasurementUnitSuggestionDao::class
    factoryOf(::MeasurementUnitSuggestionSqlDelightMapper)
    factoryOf(::MeasurementUnitSuggestionRepository)

    factory<MeasurementValueQueries> { get<SqlDelightApi>().measurementValueQueries }
    factoryOf(::MeasurementValueSqlDelightDao) bind MeasurementValueDao::class
    factoryOf(::MeasurementValueSqlDelightMapper)
    factoryOf(::MeasurementValueMapper)
    factoryOf(::MeasurementValueTintMapper)
    factoryOf(::MeasurementValueRepository)

    factoryOf(::OpenFoodFactsMapper)
    factoryOf(::OpenFoodFactsApi) bind FoodApi::class
    factory<FoodQueries> { get<SqlDelightApi>().foodQueries }
    factoryOf(::FoodSqlDelightDao) bind FoodDao::class
    factoryOf(::FoodSqlDelightMapper)
    factoryOf(::FoodRepository)

    factory<FoodEatenQueries> { get<SqlDelightApi>().foodEatenQueries }
    factoryOf(::FoodEatenSqlDelightDao) bind FoodEatenDao::class
    factoryOf(::FoodEatenSqlDelightMapper)
    factoryOf(::FoodEatenRepository)

    factory<TagQueries> { get<SqlDelightApi>().tagQueries }
    factoryOf(::TagSqlDelightDao) bind TagDao::class
    factoryOf(::TagSqlDelightMapper)
    factoryOf(::TagRepository)

    factory<EntryTagQueries> { get<SqlDelightApi>().entryTagQueries }
    factoryOf(::EntryTagSqlDelightDao) bind EntryTagDao::class
    factoryOf(::EntryTagSqlDelightMapper)
    factoryOf(::EntryTagRepository)

    factoryOf(::PreferenceDao)
    factoryOf(::PreferenceRepository)

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

    singleOf(::Navigation)
}

expect fun dataPlatformModule(): Module