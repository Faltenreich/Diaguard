package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.data.entry.EntryDao
import com.faltenreich.diaguard.data.entry.EntrySqlDelightDao
import com.faltenreich.diaguard.data.entry.EntrySqlDelightMapper
import com.faltenreich.diaguard.data.entry.tag.EntryTagDao
import com.faltenreich.diaguard.data.entry.tag.EntryTagSqlDelightDao
import com.faltenreich.diaguard.data.entry.tag.EntryTagSqlDelightMapper
import com.faltenreich.diaguard.data.food.FoodDao
import com.faltenreich.diaguard.data.food.FoodSqlDelightDao
import com.faltenreich.diaguard.data.food.FoodSqlDelightMapper
import com.faltenreich.diaguard.data.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.data.food.eaten.FoodEatenSqlDelightDao
import com.faltenreich.diaguard.data.food.eaten.FoodEatenSqlDelightMapper
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategorySqlDelightDao
import com.faltenreich.diaguard.data.measurement.category.MeasurementCategorySqlDelightMapper
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertySqlDelightDao
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertySqlDelightMapper
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitSqlDelightDao
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitSqlDelightMapper
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionDao
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionSqlDelightDao
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionSqlDelightMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueSqlDelightDao
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueSqlDelightMapper
import com.faltenreich.diaguard.data.tag.TagDao
import com.faltenreich.diaguard.data.tag.TagSqlDelightDao
import com.faltenreich.diaguard.data.tag.TagSqlDelightMapper
import com.faltenreich.diaguard.persistence.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.persistence.sqldelight.sqlDelightModule
import com.faltenreich.diaguard.persistence.sqlite.sqliteModule
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun databaseModule() = module {
    includes(sqliteModule())
    includes(sqlDelightModule(inMemory = false))

    singleOf(::SqlDelightDatabase)

    factoryOf(::EntrySqlDelightMapper)
    factoryOf(::EntrySqlDelightDao) bind EntryDao::class

    factoryOf(::MeasurementCategorySqlDelightMapper)
    factoryOf(::MeasurementCategorySqlDelightDao) bind MeasurementCategoryDao::class

    factoryOf(::MeasurementPropertySqlDelightMapper)
    factoryOf(::MeasurementPropertySqlDelightDao) bind MeasurementPropertyDao::class

    factoryOf(::MeasurementUnitSqlDelightMapper)
    factoryOf(::MeasurementUnitSqlDelightDao) bind MeasurementUnitDao::class

    factoryOf(::MeasurementUnitSuggestionSqlDelightMapper)
    factoryOf(::MeasurementUnitSuggestionSqlDelightDao) bind MeasurementUnitSuggestionDao::class

    factoryOf(::MeasurementValueSqlDelightMapper)
    factoryOf(::MeasurementValueSqlDelightDao) bind MeasurementValueDao::class

    factoryOf(::FoodSqlDelightMapper)
    factoryOf(::FoodSqlDelightDao) bind FoodDao::class

    factoryOf(::FoodEatenSqlDelightMapper)
    factoryOf(::FoodEatenSqlDelightDao) bind FoodEatenDao::class

    factoryOf(::TagSqlDelightMapper)
    factoryOf(::TagSqlDelightDao) bind TagDao::class

    factoryOf(::EntryTagSqlDelightMapper)
    factoryOf(::EntryTagSqlDelightDao) bind EntryTagDao::class
}