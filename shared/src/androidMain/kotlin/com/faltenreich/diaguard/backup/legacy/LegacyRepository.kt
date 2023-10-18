package com.faltenreich.diaguard.backup.legacy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.backup.legacy.measurement.EntryLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.MeasurementValueLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.TagLegacy
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.shared.database.getDateTime
import com.faltenreich.diaguard.shared.database.getDouble
import com.faltenreich.diaguard.shared.database.getLong
import com.faltenreich.diaguard.shared.database.getString
import com.faltenreich.diaguard.shared.database.queryEach
import com.faltenreich.diaguard.shared.di.inject

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

    actual fun getEntries(): List<EntryLegacy> {
        val database = database ?: return emptyList()
        val entries = mutableListOf<EntryLegacy>()
        database.queryEach("entry") {
            entries.add(
                EntryLegacy(
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

    actual fun getMeasurementValues(): List<MeasurementValueLegacy> {
        val database = database ?: return emptyList()
        // value ids must be recreated since legacy data might be merged
        var valueId = 0L
        val autoIncrement = { valueId++ }

        val values = mutableListOf<MeasurementValueLegacy>()
        database.queryEach("bloodsugar") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("mgDl"),
                    typeKey = MeasurementType.Key.BLOOD_SUGAR,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("insulin") {
            val createdAt = getDateTime("createdAt")
            val updatedAt = getDateTime("updatedAt")
            val entryId = getLong("entry")
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("bolus"),
                    typeKey = MeasurementType.Key.INSULIN_BOLUS,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("correction"),
                    typeKey = MeasurementType.Key.INSULIN_CORRECTION,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("basal"),
                    typeKey = MeasurementType.Key.INSULIN_BASAL,
                    entryId = entryId,
                )
            )
        }
        database.queryEach("meal") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("carbohydrates"),
                    typeKey = MeasurementType.Key.MEAL,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("activity") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("minutes"),
                    typeKey = MeasurementType.Key.ACTIVITY,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("hba1c") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("percent"),
                    typeKey = MeasurementType.Key.HBA1C,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("weight") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("kilogram"),
                    typeKey = MeasurementType.Key.WEIGHT,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("pulse") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("frequency"),
                    typeKey = MeasurementType.Key.PULSE,
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("pressure") {
            val createdAt = getDateTime("createdAt")
            val updatedAt = getDateTime("updatedAt")
            val entryId = getLong("entry")
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("systolic"),
                    typeKey = MeasurementType.Key.BLOOD_PRESSURE_SYSTOLIC,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("diastolic"),
                    typeKey = MeasurementType.Key.BLOOD_PRESSURE_DIASTOLIC,
                    entryId = entryId,
                )
            )
        }
        database.queryEach("oxygensaturation") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("percent"),
                    typeKey = MeasurementType.Key.OXYGEN_SATURATION,
                    entryId = getLong("entry"),
                )
            )
        }
        return values
    }

    actual fun getTags(): List<TagLegacy> {
        val database = database ?: return emptyList()
        val tags = mutableListOf<TagLegacy>()
        database.queryEach("tag") {
            tags.add(
                TagLegacy(
                    id = getLong("_id"),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    name = getString("name"),
                )
            )
        }
        return tags
    }
}