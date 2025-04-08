package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.logging.Logger
import com.faltenreich.diaguard.tag.TagRepository

/**
 * Import from files bundled with the app
 */
class ImportSeedUseCase(
    private val seedRepository: SeedRepository,
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val unitRepository: MeasurementUnitRepository,
    private val foodRepository: FoodRepository,
    private val tagRepository: TagRepository,
) {

    operator fun invoke() {
        importCategories()
        importFood()
        importTags()
    }

    private fun importCategories() {
        val unitsBySeed = seedRepository.getUnits().map { seed ->
            val unitId = unitRepository.create(seed)
            val unit = checkNotNull(unitRepository.getById(unitId))
            Logger.debug("Imported unit ${unit.key} from seed")
            seed to unit
        }

        val categories = seedRepository.getCategories()
        categories.forEach { category ->
            val categoryId = categoryRepository.create(category)
            category.properties.forEach { property ->
                val selection = property.units.firstOrNull()
                    ?: error("Property contains no units: $property ")
                val unit = unitsBySeed.firstOrNull { (seed, _) -> seed.key == selection }?.second
                    ?: error("Property with unknown unit: $property")
                propertyRepository.create(
                    property = property,
                    categoryId = categoryId,
                    unitId = unit.id,
                )
                Logger.debug("Imported property ${property.key} from seed")
            }
        }
        Logger.info("Imported ${categories.size} categories from seed")
    }

    private fun importFood() {
        val food = seedRepository.getFood()
        food.forEach { seed ->
            foodRepository.create(seed)
            Logger.debug("Imported food ${seed.name} from seed")
        }
        Logger.info("Imported ${food.size} foods from seed")
    }

    private fun importTags() {
        val tags = seedRepository.getTags()
        tags.forEach { seed ->
            tagRepository.create(seed)
            Logger.debug("Imported tag ${seed.name} from seed")
        }
        Logger.info("Imported ${tags.size} tags from seed")
    }
}