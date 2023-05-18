package com.faltenreich.diaguard.shared.database

import com.faltenreich.diaguard.entry.EntryDao
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyDao
import com.faltenreich.diaguard.measurement.type.MeasurementTypeDao
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitDao
import com.faltenreich.diaguard.measurement.value.MeasurementValueDao
import com.faltenreich.diaguard.shared.database.sqldelight.SqlDelightDatabase
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntrySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.EntrySqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementPropertySqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementPropertySqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementTypeSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementTypeSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementUnitSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementUnitSqlDelightMapper
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementValueSqlDelightDao
import com.faltenreich.diaguard.shared.database.sqldelight.dao.MeasurementValueSqlDelightMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun databaseModule() = module {
    singleOf(::SqlDelightDatabase)

    singleOf(::EntrySqlDelightMapper)
    single<EntryDao> { EntrySqlDelightDao() }

    singleOf(::MeasurementPropertySqlDelightMapper)
    single<MeasurementPropertyDao> { MeasurementPropertySqlDelightDao() }

    singleOf(::MeasurementTypeSqlDelightMapper)
    single<MeasurementTypeDao> { MeasurementTypeSqlDelightDao() }

    singleOf(::MeasurementUnitSqlDelightMapper)
    single<MeasurementUnitDao> { MeasurementUnitSqlDelightDao() }

    singleOf(::MeasurementValueSqlDelightMapper)
    single<MeasurementValueDao> { MeasurementValueSqlDelightDao() }
}