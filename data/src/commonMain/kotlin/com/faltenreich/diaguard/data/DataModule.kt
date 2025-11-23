package com.faltenreich.diaguard.data

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
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueSqlDelightDao
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueSqlDelightMapper
import com.faltenreich.diaguard.data.tag.TagDao
import com.faltenreich.diaguard.data.tag.TagRepository
import com.faltenreich.diaguard.data.tag.TagSqlDelightDao
import com.faltenreich.diaguard.data.tag.TagSqlDelightMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    singleOf(::SqlDelightDatabase)

    factory<EntryQueries> { get<SqlDelightDatabase>().api.entryQueries }
    factoryOf(::EntrySqlDelightDao) bind EntryDao::class
    factoryOf(::EntrySqlDelightMapper)
    factoryOf(::EntryRepository)

    factory<MeasurementCategoryQueries> { get<SqlDelightDatabase>().api.measurementCategoryQueries }
    factoryOf(::MeasurementCategorySqlDelightDao) bind MeasurementCategoryDao::class
    factoryOf(::MeasurementCategorySqlDelightMapper)
    factoryOf(::MeasurementCategoryRepository)

    factory<MeasurementPropertyQueries> { get<SqlDelightDatabase>().api.measurementPropertyQueries }
    factoryOf(::MeasurementPropertySqlDelightDao) bind MeasurementPropertyDao::class
    factoryOf(::MeasurementPropertySqlDelightMapper)
    factoryOf(::MeasurementPropertyRepository)

    factory<MeasurementUnitQueries> { get<SqlDelightDatabase>().api.measurementUnitQueries }
    factoryOf(::MeasurementUnitSqlDelightDao) bind MeasurementUnitDao::class
    factoryOf(::MeasurementUnitSqlDelightMapper)
    factoryOf(::MeasurementUnitRepository)

    factory<MeasurementUnitSuggestionQueries> { get<SqlDelightDatabase>().api.measurementUnitSuggestionQueries }
    factoryOf(::MeasurementUnitSuggestionSqlDelightDao) bind MeasurementUnitSuggestionDao::class
    factoryOf(::MeasurementUnitSuggestionSqlDelightMapper)
    factoryOf(::MeasurementUnitSuggestionRepository)

    factory<MeasurementValueQueries> { get<SqlDelightDatabase>().api.measurementValueQueries }
    factoryOf(::MeasurementValueSqlDelightDao) bind MeasurementValueDao::class
    factoryOf(::MeasurementValueSqlDelightMapper)
    factoryOf(::MeasurementValueRepository)

    factoryOf(::OpenFoodFactsMapper)
    factoryOf(::OpenFoodFactsApi) bind FoodApi::class
    factory<FoodQueries> { get<SqlDelightDatabase>().api.foodQueries }
    factoryOf(::FoodSqlDelightDao) bind FoodDao::class
    factoryOf(::FoodSqlDelightMapper)
    factoryOf(::FoodRepository)

    factory<FoodEatenQueries> { get<SqlDelightDatabase>().api.foodEatenQueries }
    factoryOf(::FoodEatenSqlDelightDao) bind FoodEatenDao::class
    factoryOf(::FoodEatenSqlDelightMapper)
    factoryOf(::FoodEatenRepository)

    factory<TagQueries> { get<SqlDelightDatabase>().api.tagQueries }
    factoryOf(::TagSqlDelightDao) bind TagDao::class
    factoryOf(::TagSqlDelightMapper)
    factoryOf(::TagRepository)

    factory<EntryTagQueries> { get<SqlDelightDatabase>().api.entryTagQueries }
    factoryOf(::EntryTagSqlDelightDao) bind EntryTagDao::class
    factoryOf(::EntryTagSqlDelightMapper)
    factoryOf(::EntryTagRepository)
}