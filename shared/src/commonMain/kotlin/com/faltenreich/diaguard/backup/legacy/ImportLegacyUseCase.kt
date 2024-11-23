package com.faltenreich.diaguard.backup.legacy

import com.faltenreich.diaguard.entry.EntryRepository
import com.faltenreich.diaguard.entry.tag.EntryTagRepository
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.food.eaten.FoodEatenRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.shared.database.DatabaseKey
import com.faltenreich.diaguard.shared.keyvalue.KeyValueStore
import com.faltenreich.diaguard.shared.keyvalue.write
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.tag.TagRepository

/**
 * Import from database of previous app version
 */
class ImportLegacyUseCase(
    private val legacyRepository: LegacyRepository,
    private val keyValueStore: KeyValueStore,
    private val entryRepository: EntryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val valueRepository: MeasurementValueRepository,
    private val foodRepository: FoodRepository,
    private val foodEatenRepository: FoodEatenRepository,
    private val tagRepository: TagRepository,
    private val entryTagRepository: EntryTagRepository,
) {

    suspend operator fun invoke() {
        val preferences = legacyRepository.getPreferences()
        preferences.forEach { preference ->
            when (preference) {
                is LegacyPreference.Boolean -> keyValueStore.write(
                    key = preference.key,
                    value = preference.value,
                )
                is LegacyPreference.Int -> keyValueStore.write(
                    key = preference.key,
                    value = preference.value,
                )
                is LegacyPreference.Long -> keyValueStore.write(
                    key = preference.key,
                    value = preference.value,
                )
                is LegacyPreference.Float -> keyValueStore.write(
                    key = preference.key,
                    value = preference.value,
                )
                is LegacyPreference.String -> keyValueStore.write(
                    key = preference.key,
                    value = preference.value,
                )
            }
        }
        print(preferences)

        return

        val properties = propertyRepository.getAll()

        val entries = legacyRepository.getEntries().associateWith { legacy ->
            val id = entryRepository.create(legacy)
            checkNotNull(entryRepository.getById(id))
        }
        Logger.info("Imported ${entries.size} entries from legacy")

        val food = legacyRepository.getFood().associateWith { legacy ->
            // TODO: Skip redundant food by uuid/serverId
            val id = foodRepository.create(legacy)
            checkNotNull(foodRepository.getById(id))
        }
        Logger.info("Imported ${food.size} food from legacy")

        val tags = legacyRepository.getTags().associateWith { legacy ->
            val id = tagRepository.create(legacy)
            checkNotNull(tagRepository.getById(id))
        }
        Logger.info("Imported ${tags.size} tags from legacy")

        val values = legacyRepository.getMeasurementValues()
        values.forEach { legacy ->
            legacy.property = properties.firstOrNull {
                it.key == legacy.propertyKey
            } ?: error("No property found for key ${legacy.propertyKey}")
            legacy.entry = entries.entries.firstOrNull {
                it.key.id == legacy.entryId
            }?.value ?: error("No entry found for id ${legacy.entryId}")
            valueRepository.create(legacy)
        }
        Logger.info("Imported ${values.size} values from legacy")

        val foodEaten = legacyRepository.getFoodEaten()
        foodEaten.forEach { legacy ->
            legacy.food = food.entries.firstOrNull {
                it.key.id == legacy.foodId
            }?.value ?: error("No food found for id ${legacy.foodId}")
            legacy.entry = entries.entries.firstOrNull { entry ->
                // FIXME: Null due to skipped values with value of zero
                val value = values.firstOrNull {
                    it.propertyKey == DatabaseKey.MeasurementProperty.MEAL && it.id == legacy.mealId
                } ?: error("No meal value found with mealId ${legacy.mealId}")
                entry.key.id == value.entryId
            }?.value ?: error("No entry found for mealId ${legacy.mealId}")
            foodEatenRepository.create(legacy)
        }
        Logger.info("Imported ${foodEaten.size} food eaten from legacy")

        val entryTags = legacyRepository.getEntryTags()
        entryTags.forEach { legacy ->
            legacy.entry = entries.entries.firstOrNull {
                it.key.id == legacy.entryId
            }?.value ?: error("No entry found for id ${legacy.entryId}")
            legacy.tag = tags.entries.firstOrNull {
                it.key.id == legacy.entryId
            }?.value ?: error("No tag found for id ${legacy.tagId}")
            entryTagRepository.create(legacy)
        }
        Logger.info("Imported ${entryTags.size} entry tags from legacy")
    }
}