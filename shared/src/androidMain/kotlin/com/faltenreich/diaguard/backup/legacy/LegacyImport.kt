package com.faltenreich.diaguard.backup.legacy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.backup.legacy.measurement.EntryLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.MeasurementValueLegacy
import com.faltenreich.diaguard.backup.legacy.measurement.TagLegacy
import com.faltenreich.diaguard.shared.database.getDateTime
import com.faltenreich.diaguard.shared.database.getDouble
import com.faltenreich.diaguard.shared.database.getLong
import com.faltenreich.diaguard.shared.database.getString
import com.faltenreich.diaguard.shared.database.queryEach
import com.faltenreich.diaguard.shared.di.inject

actual class LegacyImport {

    actual operator fun invoke() {
        val context = inject<Context>()
        val databaseFile = context.getDatabasePath("diaguard.db")
        if (!databaseFile.exists()) {
            return
        }
        val database = SQLiteDatabase.openDatabase(databaseFile.absolutePath, null, 0)

        // TODO: Fetch type ids via key
        val bloodSugarTypeId = 1L
        val bolusTypeId = 2L
        val correctionTypeId = 3L
        val basalTypeId = 4L
        val mealTypeId = 5L
        val activityTypeId = 6L

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

        val values = mutableListOf<MeasurementValueLegacy>()
        // value ids must be recreated since legacy data might be merged
        var valueId = 0L
        val autoIncrement = { valueId++ }

        database.queryEach("bloodsugar") {
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("mgDl"),
                    typeId = bloodSugarTypeId,
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
                    typeId = bolusTypeId,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("correction"),
                    typeId = correctionTypeId,
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("basal"),
                    typeId = basalTypeId,
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
                    typeId = mealTypeId,
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
                    typeId = activityTypeId,
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
                    typeId = 1, // TODO: Determine typeId of percent
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
                    typeId = 1, // TODO: Determine typeId of kilogram
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
                    typeId = 1, // TODO: Determine typeId of frequency
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
                    typeId = 1, // TODO: Determine typeId of systolic
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValueLegacy(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("diastolic"),
                    typeId = 1, // TODO: Determine typeId of diastolic
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
                    typeId = 1, // TODO: Determine typeId of percent
                    entryId = getLong("entry"),
                )
            )
        }

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

        println("Found ${entries.size} entries, ${values.size} values and ${tags.size} tags")
    }
}