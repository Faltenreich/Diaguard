package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.entry.tag.EntryTagDao
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestionDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntrySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntryTagSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.FoodEatenSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.FoodSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementCategorySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementPropertySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementUnitSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementUnitSuggestionSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementValueSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.TagSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntrySqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntryTagSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.FoodEatenSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.FoodSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementCategorySqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementPropertySqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementUnitSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementUnitSuggestionSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementValueSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.TagSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.sqlDelightModule
import com.faltenreich.diaguard.shared.database.sqlite.sqliteModule
import com.faltenreich.diaguard.tag.TagDao
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