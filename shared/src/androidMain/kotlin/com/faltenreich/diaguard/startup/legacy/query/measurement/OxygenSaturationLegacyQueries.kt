package com.faltenreich.diaguard.startup.legacy.query.measurement

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.data.measurement.value.MeasurementValue
import com.faltenreich.diaguard.data.DatabaseKey
import com.faltenreich.diaguard.persistence.sqlite.SqliteDatabase
import com.faltenreich.diaguard.persistence.sqlite.getDouble
import com.faltenreich.diaguard.persistence.sqlite.getLong
import com.faltenreich.diaguard.startup.legacy.query.LegacyQueries

class OxygenSaturationLegacyQueries(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) : LegacyQueries<MeasurementValue.Legacy> {

    override fun getAll(): List<MeasurementValue.Legacy> {
        val values = mutableListOf<MeasurementValue.Legacy>()
        database.query("oxygensaturation") {
            val id = getLong("_id") ?: return@query
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("percent") ?: return@query
            values.add(
                MeasurementValue.Legacy(
                    id = id,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = value,
                    propertyKey = DatabaseKey.MeasurementProperty.OXYGEN_SATURATION,
                    entryId = entryId,
                )
            )
        }
        return values
    }
}