package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.logging.Logger

class LegacyImport(
    private val legacyRepository: LegacyRepository,
    private val entryRepository: EntryRepository,
    private val typeRepository: MeasurementTypeRepository,
    private val valueRepository: MeasurementValueRepository,
) : Import {

    override fun import() {
        val entries = legacyRepository.getEntries()
        val typeIdsByKey = typeRepository.getAll().associate { it.key to it.id }
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
                val typeKey = value.typeKey
                val typeId = typeIdsByKey[typeKey]
                checkNotNull(typeId)
                valueRepository.create(
                    createdAt = value.createdAt,
                    updatedAt = value.updatedAt,
                    value = value.value,
                    typeId = typeId,
                    entryId = entryId,
                )
            }
        }
    }
}