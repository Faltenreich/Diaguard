package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValue
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
        val properties = propertyRepository.getAll()
        val values = legacyRepository.getMeasurementValues()
        val tags = legacyRepository.getTags()

        Logger.info("Imported ${entries.size} entries, ${values.size} values and ${tags.size} tags")

        // TODO: Import tags and food

        entries.forEach { entryLegacy ->
            val entryLegacyId = entryLegacy.id
            val entryId = entryRepository.create(entryLegacy)
            val entry = checkNotNull(entryRepository.getById(entryId))
            val valuesOfEntry = values.filter { value -> value.entryId == entryLegacyId }
            valuesOfEntry.forEach { value ->
                val property = properties.first { it.key == value.propertyKey }
                valueRepository.create(
                    MeasurementValue.Legacy(
                        createdAt = value.createdAt,
                        updatedAt = value.updatedAt,
                        value = value.value,
                        property = property,
                        entry = entry,
                    )
                )
            }
        }
    }
}