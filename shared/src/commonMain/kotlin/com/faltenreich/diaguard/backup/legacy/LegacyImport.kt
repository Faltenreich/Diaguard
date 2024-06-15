package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
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
    private val foodRepository: FoodRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val tagRepository: TagRepository,
    private val entryTagRepository: EntryTagRepository,
) : Import {

    override fun import() {
        val properties = propertyRepository.getAll()

        val entries = legacyRepository.getEntries().associateWith { legacy ->
            val id = entryRepository.create(legacy)
            Logger.info("Imported entry: $legacy")
            checkNotNull(entryRepository.getById(id))
        }
        Logger.info("Imported ${entries.size} entries")

        val food = legacyRepository.getFood().associateWith { legacy ->
            // TODO: Skip redundant food by uuid/serverId
            val id = foodRepository.create(legacy)
            Logger.info("Imported food: $legacy")
            checkNotNull(foodRepository.getById(id))
        }
        Logger.info("Imported ${food.size} food")

        val tags = legacyRepository.getTags().associateWith { legacy ->
            val id = tagRepository.create(legacy)
            Logger.info("Imported tag: $legacy")
            checkNotNull(tagRepository.getById(id))
        }
        Logger.info("Imported ${tags.size} tags")

        val values = legacyRepository.getMeasurementValues()
        values.forEach { legacy ->
            legacy.property = properties.first { it.key == legacy.propertyKey }
            legacy.entry = entries.entries.first { it.key.id == legacy.entryId }.value
            valueRepository.create(legacy)
            Logger.info("Imported measurement value: $legacy")
        }

        legacyRepository.getFoodEaten().forEach { legacy ->
            legacy.food = food.entries.first { it.key.id == legacy.foodId }.value
            legacy.entry = entries.entries.first { entry ->
                val value = values.first {
                    it.propertyKey == DatabaseKey.MeasurementProperty.MEAL && it.id == legacy.mealId
                }
                entry.key.id == value.entryId
            }.value
            foodEatenRepository.create(legacy)
            Logger.info("Imported food eaten: $legacy")
        }

        legacyRepository.getEntryTags().forEach { legacy ->
            legacy.entry = entries.entries.first { it.key.id == legacy.entryId }.value
            legacy.tag = tags.entries.first { it.key.id == legacy.entryId }.value
            entryTagRepository.create(legacy)
            Logger.info("Imported entry tag: $legacy")
        }
    }
}