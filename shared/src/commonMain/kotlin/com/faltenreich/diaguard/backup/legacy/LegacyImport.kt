package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
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
    private val foodRepository: FoodRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val tagRepository: TagRepository,
    private val entryTagRepository: EntryTagRepository,
) : Import {

    override fun import() {
        val properties = propertyRepository.getAll()

        val entriesByLegacyId = legacyRepository.getEntries().associate { legacy ->
            val id = entryRepository.create(legacy)
            val entry = checkNotNull(entryRepository.getById(id))
            Logger.info("Imported entry: $legacy")
            legacy.id to entry
        }
        Logger.info("Imported ${entriesByLegacyId.size} entries")

        val foodByLegacyId = legacyRepository.getFood().associate { legacy ->
            // TODO: Skip redundant food by uuid/serverId
            val id = foodRepository.create(legacy)
            val food = checkNotNull(foodRepository.getById(id))
            Logger.info("Imported food: $legacy")
            legacy.id to food
        }
        Logger.info("Imported ${foodByLegacyId.size} food")

        val tagsByLegacyId = legacyRepository.getTags().associate { legacy ->
            val id = tagRepository.create(legacy)
            val tag = checkNotNull(tagRepository.getById(id))
            Logger.info("Imported tag: $legacy")
            legacy.id to tag
        }
        Logger.info("Imported ${tagsByLegacyId.size} tags")

        legacyRepository.getMeasurementValues().forEach { legacy ->
            legacy.property = properties.first { it.key == legacy.propertyKey }
            legacy.entry = checkNotNull(entriesByLegacyId[legacy.entryId])
            valueRepository.create(legacy)
            Logger.info("Imported measurement value: $legacy")
        }

        legacyRepository.getFoodEaten().forEach { legacy ->
            legacy.food = checkNotNull(foodByLegacyId[legacy.foodId])
            // FIXME: Retrieve entry via legacy.mealId
            // legacy.entry = checkNotNull(entriesByLegacyId[legacy.])
            foodEatenRepository.create(legacy)
            Logger.info("Imported food eaten: $legacy")
        }

        legacyRepository.getEntryTags().forEach { legacy ->
            legacy.entry = checkNotNull(entriesByLegacyId[legacy.entryId])
            legacy.tag = checkNotNull(tagsByLegacyId[legacy.tagId])
            entryTagRepository.create(legacy)
            Logger.info("Imported entry tag: $legacy")
        }
    }
}