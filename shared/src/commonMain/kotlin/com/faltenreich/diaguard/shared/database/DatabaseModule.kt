package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.food.FoodDao
import com.faltenreich.diaguard.food.eaten.FoodEatenDao
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryDao
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntrySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntryTagSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.FoodEatenSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.FoodSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementCategorySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementTypeSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementUnitSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementValueSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.TagSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntrySqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.EntryTagSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.FoodEatenSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.FoodSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementCategorySqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementTypeSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementUnitSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.MeasurementValueSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.mapper.TagSqlDelightMapper
import com.faltenreich.diaguard.tag.EntryTagDao
import com.faltenreich.diaguard.tag.TagDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun databaseModule() = module {
    singleOf(::SqlDelightDatabase)

    singleOf(::EntrySqlDelightMapper)
    single<EntryDao> { EntrySqlDelightDao() }

    singleOf(::MeasurementCategorySqlDelightMapper)
    single<MeasurementCategoryDao> { MeasurementCategorySqlDelightDao() }

    singleOf(::MeasurementTypeSqlDelightMapper)
    single<MeasurementTypeDao> { MeasurementTypeSqlDelightDao() }

    singleOf(::MeasurementUnitSqlDelightMapper)
    single<MeasurementUnitDao> { MeasurementUnitSqlDelightDao() }

    singleOf(::MeasurementValueSqlDelightMapper)
    single<MeasurementValueDao> { MeasurementValueSqlDelightDao() }

    singleOf(::FoodSqlDelightMapper)
    single<FoodDao> { FoodSqlDelightDao() }

    singleOf(::FoodEatenSqlDelightMapper)
    single<FoodEatenDao> { FoodEatenSqlDelightDao() }

    singleOf(::TagSqlDelightMapper)
    single<TagDao> { TagSqlDelightDao() }

    singleOf(::EntryTagSqlDelightMapper)
    single<EntryTagDao> { EntryTagSqlDelightDao() }
}