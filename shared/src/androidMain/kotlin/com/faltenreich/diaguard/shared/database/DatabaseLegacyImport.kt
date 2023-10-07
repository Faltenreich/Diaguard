package com.faltenreich.diaguard.shared.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.measurement.value.MeasurementValue
import com.faltenreich.diaguard.shared.di.inject

actual class DatabaseLegacyImport {

    actual fun import() {
        val context = inject<Context>()
        val databaseFile = context.getDatabasePath("diaguard.db")
        // TODO: Return if file does not exist
        val database = SQLiteDatabase.openDatabase(databaseFile.absolutePath, null, 0)

        val entries = mutableListOf<Entry>()
        database.queryEach("entry") {
            entries.add(
                Entry(
                    id = getLong("_id"),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    dateTime = getDateTime("date"),
                    note = getString("note"),
                )
            )
        }

        val values = mutableListOf<MeasurementValue>()
        // value ids must be recreated since legacy data might be merged
        var valueId = 0L
        val autoIncrement = { valueId++ }

        database.queryEach("bloodsugar") {
            values.add(
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("mgDl"),
                    typeId = 1, // TODO: Determine typeId of mgDl
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("insulin") {
            val createdAt = getDateTime("createdAt")
            val updatedAt = getDateTime("updatedAt")
            val entryId = getLong("entry")
            values.add(
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("bolus"),
                    typeId = 1, // TODO: Determine typeId of bolus
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("basal"),
                    typeId = 1, // TODO: Determine typeId of basal
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("correction"),
                    typeId = 1, // TODO: Determine typeId of correction
                    entryId = entryId,
                )
            )
        }
        database.queryEach("meal") {
            values.add(
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("carbohydrates"),
                    typeId = 1, // TODO: Determine typeId of carbohydrates
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("activity") {
            values.add(
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("minutes"),
                    typeId = 1, // TODO: Determine typeId of minutes
                    entryId = getLong("entry"),
                )
            )
        }
        database.queryEach("hba1c") {
            values.add(
                MeasurementValue(
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
                MeasurementValue(
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
                MeasurementValue(
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
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    value = getDouble("systolic"),
                    typeId = 1, // TODO: Determine typeId of systolic
                    entryId = entryId,
                )
            )
            values.add(
                MeasurementValue(
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
                MeasurementValue(
                    id = autoIncrement(),
                    createdAt = getDateTime("createdAt"),
                    updatedAt = getDateTime("updatedAt"),
                    value = getDouble("percent"),
                    typeId = 1, // TODO: Determine typeId of percent
                    entryId = getLong("entry"),
                )
            )
        }

        val tags = mutableListOf<String>()
        database.queryEach("tag") {
            val id = getLong("_id")
            val createdAt = getDateTime("createdAt")
            val updatedAt = getDateTime("updatedAt")
            val name = getString("name")
            tags.add(name)
        }

        println("Found ${entries.size} entries, ${values.size} values and ${tags.size} tags")
    }
}