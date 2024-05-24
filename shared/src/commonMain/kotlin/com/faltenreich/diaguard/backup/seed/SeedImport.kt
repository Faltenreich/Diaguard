package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.TagRepository

/**
 * Import from files bundled with the app
 */
class SeedImport(
    private val localization: Localization,
    private val seedRepository: SeedRepository,
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val unitRepository: MeasurementUnitRepository,
    private val foodRepository: FoodRepository,
    private val tagRepository: TagRepository,
) : Import {

    override fun import() {
        val categorySeeds = seedRepository.getMeasurementCategories()
        categorySeeds.forEachIndexed { categorySortIndex, categorySeed ->
            val categoryId = categoryRepository.create(
                MeasurementCategory.Seed(
                    key = categorySeed.key,
                    name = localization.getString(categorySeed.name),
                    icon = categorySeed.icon,
                    sortIndex = categorySortIndex.toLong(),
                    isActive = true,
                )
            )
            categorySeed.properties.forEachIndexed { propertySortIndex, propertySeed ->
                val propertyId = propertyRepository.create(
                    MeasurementProperty.Seed(
                        key = propertySeed.key,
                        name = localization.getString(propertySeed.name),
                        sortIndex = propertySortIndex.toLong(),
                        aggregationStyle = propertySeed.aggregationStyle,
                        range = propertySeed.range,
                        categoryId = categoryId,
                    ),
                )
                propertySeed.units.forEach { unitSeed ->
                    unitRepository.create(
                        MeasurementUnit.Seed(
                            key = unitSeed.key,
                            name = localization.getString(unitSeed.name),
                            abbreviation = localization.getString(unitSeed.abbreviation),
                            factor = unitSeed.factor,
                            isSelected = unitSeed.factor == MeasurementUnit.FACTOR_DEFAULT,
                            propertyId = propertyId,
                        ),
                    )
                }
            }
        }

        val foodSeeds = seedRepository.getFood()
        foodSeeds.forEach { foodSeed ->
            val food = Food.User(
                name = foodSeed.en, // TODO: Localize
                brand = null,
                ingredients = null,
                labels = null, // TODO: Mark seed
                carbohydrates = foodSeed.carbohydrates.toDouble(),
                energy = foodSeed.energy.toDoubleOrNull(),
                fat = foodSeed.fat.toDoubleOrNull(),
                fatSaturated = foodSeed.fatSaturated.toDoubleOrNull(),
                fiber = foodSeed.fiber.toDoubleOrNull(),
                proteins = foodSeed.proteins.toDoubleOrNull(),
                salt = foodSeed.salt.toDoubleOrNull(),
                sodium = foodSeed.sodium.toDoubleOrNull(),
                sugar = foodSeed.sugar.toDoubleOrNull(),
            )
            foodRepository.create(food)
        }

        val tagSeeds = seedRepository.getTags()
        tagSeeds.forEach { tagSeed ->
            val tag = Tag.User(name = tagSeed.en) // TODO: Localize
            tagRepository.create(tag)
        }
    }
}