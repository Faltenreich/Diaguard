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
class SeedImport(
    private val seedRepository: SeedRepository,
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val unitRepository: MeasurementUnitRepository,
    private val foodRepository: FoodRepository,
    private val tagRepository: TagRepository,
) {

    fun import() {
        seedRepository.getMeasurementCategories().forEach { category ->
            val categoryId = categoryRepository.create(category)
            Logger.info("Imported category: $category")
            category.properties.forEach { property ->
                val propertyId = propertyRepository.create(property, categoryId)
                Logger.info("Imported property: $property")
                property.units.forEach { unit ->
                    unitRepository.create(unit, propertyId)
                    Logger.info("Imported unit: $unit")
                }
            }
        }

        seedRepository.getFood().forEach { seed ->
            foodRepository.create(seed)
            Logger.info("Imported food: $seed")
        }

        seedRepository.getTags().forEach { seed ->
            tagRepository.create(seed)
            Logger.info("Imported tag: $seed")
        }
    }
}