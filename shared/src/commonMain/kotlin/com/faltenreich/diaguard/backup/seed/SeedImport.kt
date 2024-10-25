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
        val measurements = seedRepository.getMeasurementCategories()
        measurements.forEach { category ->
            val categoryId = categoryRepository.create(category)
            category.properties.forEach { property ->
                val propertyId = propertyRepository.create(property, categoryId)
                property.units.forEach { unit ->
                    unitRepository.create(unit, propertyId)
                }
            }
        }
        Logger.info("Imported ${measurements.size} categories from seed")

        val foods = seedRepository.getFood()
        foods.forEach { food ->
            foodRepository.create(food)
        }
        Logger.info("Imported ${foods.size} foods from seed")

        val tags = seedRepository.getTags()
        tags.forEach { tag ->
            tagRepository.create(tag)
        }
        Logger.info("Imported ${tags.size} tags from seed")
    }
}