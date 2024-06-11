package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqlite.SqliteApi
import com.faltenreich.diaguard.tag.Tag

class LegacyDatabase(
    private val sqliteApi: SqliteApi,
    private val dateTimeFactory: DateTimeFactory,
) : LegacyDao {

    override fun getEntries(): List<Entry.Legacy> {
        val entries = mutableListOf<Entry.Legacy>()
        sqliteApi.queryEach("entry") {
            val id = getLong("_id") ?: return@queryEach
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val dateTime = getLong("date")?.let(dateTimeFactory::dateTime) ?: return@queryEach
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

        sqliteApi.queryEach("bloodsugar") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            val value = getDouble("mgDl") ?: return@queryEach
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
        sqliteApi.queryEach("insulin") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            getDouble("bolus")?.let { value ->
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
            getDouble("correction")?.let { value ->
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
            getDouble("basal")?.let { value ->
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
        sqliteApi.queryEach("meal") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            val value = getDouble("carbohydrates") ?: return@queryEach
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
        sqliteApi.queryEach("activity") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            val value = getDouble("minutes") ?: return@queryEach
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
        sqliteApi.queryEach("hba1c") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            val value = getDouble("percent") ?: return@queryEach
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
        sqliteApi.queryEach("weight") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            val value = getDouble("kilogram") ?: return@queryEach
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
        sqliteApi.queryEach("pulse") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            val value = getDouble("frequency") ?: return@queryEach
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
        sqliteApi.queryEach("pressure") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            getDouble("systolic")?.let { value ->
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
            getDouble("diastolic")?.let { value ->
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
        sqliteApi.queryEach("oxygensaturation") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val entryId = getLong("entry") ?: return@queryEach
            val value = getDouble("percent") ?: return@queryEach
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
        sqliteApi.queryEach("tag") {
            val createdAt = getLong("createdAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val updatedAt = getLong("updatedAt")?.let(dateTimeFactory::dateTime) ?: return@queryEach
            val name = getString("name") ?: return@queryEach
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