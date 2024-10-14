package com.faltenreich.diaguard.backup.legacy.query.measurement

import com.faltenreich.diaguard.backup.legacy.query.LegacyQueries
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getDouble
import com.faltenreich.diaguard.shared.database.sqlite.getLong

class InsulinLegacyQueries(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) : LegacyQueries<MeasurementValue.Legacy> {

    override fun getAll(): List<MeasurementValue.Legacy> {
        val values = mutableListOf<MeasurementValue.Legacy>()
        database.query("insulin") {
            val id = getLong("_id") ?: return@query
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            getDouble("bolus")?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        id = id,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                        entryId = entryId,
                    )
                )
            }
            getDouble("correction")?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        id = id,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                        entryId = entryId,
                    )
                )
            }
            getDouble("basal")?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        id = id,
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                        entryId = entryId,
                    )
                )
            }
        }
        return values
    }
}