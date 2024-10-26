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
class SeedImportUseCase(
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
        val categories = seedRepository.getMeasurementCategories()
        categories.forEach { category ->
            val categoryId = categoryRepository.create(category)
            category.properties.forEach { property ->
                val propertyId = propertyRepository.create(property, categoryId)
                property.units.forEach { unit ->
                    unitRepository.create(unit, propertyId)
                }
            }
        }
        Logger.info("Imported ${categories.size} categories from seed")
    }

    private fun importFood() {
        val food = seedRepository.getFood()
        food.forEach(foodRepository::create)
        Logger.info("Imported ${food.size} foods from seed")
    }

    private fun importTags() {
        val tags = seedRepository.getTags()
        tags.forEach(tagRepository::create)
        Logger.info("Imported ${tags.size} tags from seed")
    }
}