package com.faltenreich.diaguard.backup.legacy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.database.sqlite.getDateTime
import com.faltenreich.diaguard.shared.database.sqlite.getDouble
import com.faltenreich.diaguard.shared.database.sqlite.getLong
import com.faltenreich.diaguard.shared.database.sqlite.getString
import com.faltenreich.diaguard.shared.database.sqlite.queryEach
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag

actual class LegacyRepository {

    private val database: SQLiteDatabase?

    init {
        val context = inject<Context>()
        val databaseFile = context.getDatabasePath("diaguard.db")
        database = if (databaseFile.exists()) {
            SQLiteDatabase.openDatabase(databaseFile.absolutePath, null, 0)
        } else {
            null
        }
    }

    actual fun getEntries(): List<Entry.Legacy> {
        val database = database ?: return emptyList()
        val entries = mutableListOf<Entry.Legacy>()
        database.queryEach("entry") {
            entries.add(
                Entry.Legacy(
                    id = getLong("_id"),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    dateTime = getDateTime("date"),
                    note = getString("note"),
                )
            )
        }
        return entries
    }

    actual fun getMeasurementValues(): List<MeasurementValue.Legacy> {
        val database = database ?: return emptyList()
        val values = mutableListOf<MeasurementValue.Legacy>()

        database.queryEach("bloodsugar") {
            values.add(
                MeasurementValue.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("mgDl"),
                    propertyKey = DatabaseKey.MeasurementProperty.BLOOD_SUGAR,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("insulin") {
            val createdAt = getDateTime("createdAt")
            val updatedAt = getDateTime("updatedAt")
            val entryId = getLong("entry")
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("bolus"),
                    propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BOLUS,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("correction"),
                    propertyKey = DatabaseKey.MeasurementProperty.INSULIN_CORRECTION,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("basal"),
                    propertyKey = DatabaseKey.MeasurementProperty.INSULIN_BASAL,
                    entryId = entryId,
                )
            )
        }
        database.queryEach("meal") {
            values.add(
                MeasurementValue.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("carbohydrates"),
                    propertyKey = DatabaseKey.MeasurementProperty.MEAL,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("activity") {
            values.add(
                MeasurementValue.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("minutes"),
                    propertyKey = DatabaseKey.MeasurementProperty.ACTIVITY,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("hba1c") {
            values.add(
                MeasurementValue.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("percent"),
                    propertyKey = DatabaseKey.MeasurementProperty.HBA1C,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("weight") {
            values.add(
                MeasurementValue.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("kilogram"),
                    propertyKey = DatabaseKey.MeasurementProperty.WEIGHT,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("pulse") {
            values.add(
                MeasurementValue.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("frequency"),
                    propertyKey = DatabaseKey.MeasurementProperty.PULSE,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("pressure") {
            val createdAt = getDateTime("createdAt")
            val updatedAt = getDateTime("updatedAt")
            val entryId = getLong("entry")
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("systolic"),
                    propertyKey = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_SYSTOLIC,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValue.Legacy(
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("diastolic"),
                    propertyKey = DatabaseKey.MeasurementProperty.BLOOD_PRESSURE_DIASTOLIC,
                    entryId = entryId,
                )
            )
        }
        database.queryEach("oxygensaturation") {
            values.add(
                MeasurementValue.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("percent"),
                    propertyKey = DatabaseKey.MeasurementProperty.OXYGEN_SATURATION,
                    entryId = getLong("entry"),
                )
            )
        }
        return values
    }

    actual fun getTags(): List<Tag.Legacy> {
        val database = database ?: return emptyList()
        val tags = mutableListOf<Tag.Legacy>()
        database.queryEach("tag") {
            tags.add(
                Tag.Legacy(
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    name = getString("name"),
                )
            )
        }
        return tags
    }
}