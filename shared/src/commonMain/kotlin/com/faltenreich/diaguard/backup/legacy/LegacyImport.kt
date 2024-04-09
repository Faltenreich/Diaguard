package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.logging.Logger

class LegacyImport(
    private val legacyRepository: LegacyRepository,
    private val entryRepository: EntryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
) : Import {

    override fun import() {
        val entries = legacyRepository.getEntries()
        val propertyIdsByKey = propertyRepository.getAll().associate { it.key to it.id }
        val values = legacyRepository.getMeasurementValues()
        val tags = legacyRepository.getTags()

        Logger.info("Imported ${entries.size} entries, ${values.size} values and ${tags.size} tags")

        entries.forEach { entry ->
            val entryLegacyId = entry.id
            val entryId = entryRepository.create(
                createdAt = entry.createdAt,
                updatedAt = entry.updatedAt,
                dateTime = entry.dateTime,
                note = entry.note,
            )
            val valuesOfEntry = values.filter { value -> value.entryId == entryLegacyId }
            valuesOfEntry.forEach { value ->
                val propertyKey = value.propertyKey
                val propertyId = propertyIdsByKey[propertyKey]
                checkNotNull(propertyId)
                valueRepository.create(
                    createdAt = value.createdAt,
                    updatedAt = value.updatedAt,
                    value = value.value,
                    propertyId = propertyId,
                    entryId = entryId,
                )
            }
        }
    }
}