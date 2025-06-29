package com.faltenreich.diaguard.startup.seed

import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestion
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestionRepository
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
    private val unitSuggestionRepository: MeasurementUnitSuggestionRepository,
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
            Logger.verbose("Imported unit from seed: $unit")
            seed to unit
        }

        val categories = seedRepository.getCategories()
        categories.forEach { category ->
            val categoryId = categoryRepository.create(category)
            val propertyCategory = checkNotNull(categoryRepository.getById(categoryId))

            category.properties.forEach { property ->
                val propertyUnitSuggestion = property.unitSuggestions
                    .firstOrNull(MeasurementUnitSuggestion.Seed::isDefault)
                    ?: error("Property contains no units: $property ")
                val propertyUnit = unitsBySeed
                    .firstOrNull { (seed, _) -> seed.key == propertyUnitSuggestion.unit }?.second
                    ?: error("Property contains unknown unit: $property")

                property.category = propertyCategory
                property.unit = propertyUnit

                val propertyId = propertyRepository.create(property)
                    ?: error("Property could not be created: $property ")
                Logger.verbose("Imported property from seed: $property")

                property.unitSuggestions.forEach { unitSuggestion ->
                    val unit = unitsBySeed
                        .firstOrNull { (seed, _) -> seed.key == unitSuggestion.unit }?.second
                        ?: error("Unit suggestion contains unknown unit: $property")
                    unitSuggestionRepository.create(
                        unitSuggestion = unitSuggestion,
                        propertyId = propertyId,
                        unitId = unit.id,
                    )
                    Logger.verbose("Imported unit suggestion from seed: $unitSuggestion")
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
        tags.forEach { seed ->
            tagRepository.create(seed)
            Logger.verbose("Imported tag from seed: $seed")
        }
        Logger.info("Imported ${tags.size} tags from seed")
    }
}