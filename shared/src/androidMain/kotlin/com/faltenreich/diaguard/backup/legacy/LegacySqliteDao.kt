package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqlite.SqliteDatabase
import com.faltenreich.diaguard.shared.database.sqlite.getDouble
import com.faltenreich.diaguard.shared.database.sqlite.getLong
import com.faltenreich.diaguard.shared.database.sqlite.getString
import com.faltenreich.diaguard.tag.Tag

actual class LegacySqliteDao(
    private val database: SqliteDatabase,
    private val dateTimeFactory: DateTimeFactory,
) : LegacyDao {

    override fun getEntries(): List<Entry.Legacy> {
        val entries = mutableListOf<Entry.Legacy>()
        database.query("entry") {
            val id = getLong("_id") ?: return@query
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val dateTime = getLong("date")?.let(dateTimeFactory::dateTime) ?: return@query
            entries.add(
                Entry.Legacy(
                    id = id,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    dateTime = dateTime,
                    note = getString("note"),
                )
            )
        }
        return entries
    }

    override fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        val values = mutableListOf<MeasurementValue.Legacy>()

        database.query("bloodsugar") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("mgDl")?.takeIf { it > 0 } ?: return@query
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = value,
                    propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                    entryId = entryId,
                )
            )
        }
        database.query("insulin") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            getDouble("bolus")?.takeIf { it > 0 }?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                        entryId = entryId,
                    )
                )
            }
            getDouble("correction")?.takeIf { it > 0 }?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                        entryId = entryId,
                    )
                )
            }
            getDouble("basal")?.takeIf { it > 0 }?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                        entryId = entryId,
                    )
                )
            }
        }
        database.query("meal") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("carbohydrates")?.takeIf { it > 0 } ?: return@query
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = value,
                    propertyKey = DatabaseKey.MeasurementProperty.MEAL,
                    entryId = entryId,
                )
            )
        }
        database.query("activity") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("minutes")?.takeIf { it > 0 } ?: return@query
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = value,
                    propertyKey = DatabaseKey.MeasurementProperty.ACTIVITY,
                    entryId = entryId,
                )
            )
        }
        database.query("hba1c") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("percent")?.takeIf { it > 0 } ?: return@query
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = value,
                    propertyKey = DatabaseKey.MeasurementProperty.HBA1C,
                    entryId = entryId,
                )
            )
        }
        database.query("weight") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("kilogram")?.takeIf { it > 0 } ?: return@query
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = value,
                    propertyKey = DatabaseKey.MeasurementProperty.WEIGHT,
                    entryId = entryId,
                )
            )
        }
        database.query("pulse") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("frequency")?.takeIf { it > 0 } ?: return@query
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = value,
                    propertyKey = DatabaseKey.MeasurementProperty.PULSE,
                    entryId = entryId,
                )
            )
        }
        database.query("pressure") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            getDouble("systolic")?.takeIf { it > 0 }?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC,
                        entryId = entryId,
                    )
                )
            }
            getDouble("diastolic")?.takeIf { it > 0 }?.let { value ->
                values.add(
                    MeasurementValue.Legacy(
                        createdAt = createdAt,
                        updatedAt = updatedAt,
                        value = value,
                        propertyKey = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC,
                        entryId = entryId,
                    )
                )
            }
        }
        database.query("oxygensaturation") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val entryId = getLong("entry") ?: return@query
            val value = getDouble("percent")?.takeIf { it > 0 } ?: return@query
            values.add(
                MeasurementValue.Legacy(
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

    override fun getTags(): List<Tag.Legacy> {
        val tags = mutableListOf<Tag.Legacy>()
        database.query("tag") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@query
            val name = getString("name") ?: return@query
            tags.add(
                Tag.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    name = name,
                )
            )
        }
        return tags
    }
}