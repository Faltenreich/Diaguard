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
        val properties = propertyRepository.getAll()

        val entries = legacyRepository.getEntries()
        val values = legacyRepository.getMeasurementValues()

        entries.forEach { entryLegacy ->
            val entryLegacyId = entryLegacy.id
            val entryId = entryRepository.create(entryLegacy)
            val entry = checkNotNull(entryRepository.getById(entryId))
            val valuesOfEntry = values.filter { value -> value.entryId == entryLegacyId }

            valuesOfEntry.forEach { value ->
                value.property = properties.first { it.key == value.propertyKey }
                value.entry = entry

                valueRepository.create(value)
            }
        }

        // TODO: Import tags and food
        val tags = legacyRepository.getTags()

        Logger.info("Imported ${entries.size} entries, ${values.size} values and ${tags.size} tags")
    }
}