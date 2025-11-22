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

    factoryOf(::EntrySqlDelightMapper)
    factoryOf(::EntrySqlDelightDao) bind EntryDao::class
    factoryOf(::EntryRepository)

    factoryOf(::MeasurementCategorySqlDelightMapper)
    factoryOf(::MeasurementCategorySqlDelightDao) bind MeasurementCategoryDao::class
    factoryOf(::MeasurementCategoryRepository)

    factoryOf(::MeasurementPropertySqlDelightMapper)
    factoryOf(::MeasurementPropertySqlDelightDao) bind MeasurementPropertyDao::class
    factoryOf(::MeasurementPropertyRepository)

    factoryOf(::MeasurementUnitSqlDelightMapper)
    factoryOf(::MeasurementUnitSqlDelightDao) bind MeasurementUnitDao::class
    factoryOf(::MeasurementUnitRepository)

    factoryOf(::MeasurementUnitSuggestionSqlDelightMapper)
    factoryOf(::MeasurementUnitSuggestionSqlDelightDao) bind MeasurementUnitSuggestionDao::class
    factoryOf(::MeasurementUnitSuggestionRepository)

    factoryOf(::MeasurementValueSqlDelightMapper)
    factoryOf(::MeasurementValueSqlDelightDao) bind MeasurementValueDao::class
    factoryOf(::MeasurementValueRepository)

    factoryOf(::OpenFoodFactsMapper)
    factoryOf(::OpenFoodFactsApi) bind FoodApi::class
    factoryOf(::FoodSqlDelightMapper)
    factoryOf(::FoodSqlDelightDao) bind FoodDao::class
    factoryOf(::FoodRepository)

    factoryOf(::FoodEatenSqlDelightMapper)
    factoryOf(::FoodEatenSqlDelightDao) bind FoodEatenDao::class
    factoryOf(::FoodEatenRepository)

    factoryOf(::TagSqlDelightMapper)
    factoryOf(::TagSqlDelightDao) bind TagDao::class
    factoryOf(::TagRepository)

    factoryOf(::EntryTagSqlDelightMapper)
    factoryOf(::EntryTagSqlDelightDao) bind EntryTagDao::class
    factoryOf(::EntryTagRepository)
}