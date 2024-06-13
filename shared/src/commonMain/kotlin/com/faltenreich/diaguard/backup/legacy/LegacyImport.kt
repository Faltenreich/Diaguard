package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.tag.TagRepository

/**
 * Import from database of previous app version
 */
class LegacyImport(
    private val legacyRepository: LegacyRepository,
    private val entryRepository: EntryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val tagRepository: TagRepository,
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
        Logger.info("Imported ${entries.size} entries with ${values.size} values")

        // TODO: Find way to match legacy with new entity

        val food = legacyRepository.getFood()
        val foodEaten = legacyRepository.getFoodEaten()
        Logger.info("Imported ${food.size} food with ${foodEaten.size} food eaten")

        val tags = legacyRepository.getTags()
        val entryTags = legacyRepository.getEntryTags()
        tags.forEach { tag ->
            val tagLegacyId = tag.id
            val tagId = tagRepository.getByName(tag.name)?.id ?: tagRepository.create(tag)
        }
        Logger.info("Imported ${tags.size} tags with ${entryTags.size} entry tags")
    }
}