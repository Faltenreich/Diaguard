package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.tag.TagRepository

class SeedImport(
    private val localization: Localization,
    private val dateTimeFactory: DateTimeFactory,
    private val seedRepository: SeedRepository,
    private val categoryRepository: MeasurementCategoryRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val unitRepository: MeasurementUnitRepository,
    private val foodRepository: FoodRepository,
    private val tagRepository: TagRepository,
) : Import {

    override fun import() {
        val categorySeeds = seedRepository.getMeasurementCategories()
        categorySeeds.forEachIndexed { categorySortIndex, category ->
            val categoryId = categoryRepository.create(
                createdAt = dateTimeFactory.now(),
                updatedAt = dateTimeFactory.now(),
                key = category.key.key,
                name = localization.getString(category.name),
                icon = category.icon,
                sortIndex = categorySortIndex.toLong(),
                isActive = true,
            )
            category.properties.forEachIndexed { propertySortIndex, property ->
                val propertyId = propertyRepository.create(
                    createdAt = dateTimeFactory.now(),
                    updatedAt = dateTimeFactory.now(),
                    key = property.key.key,
                    name = localization.getString(property.name),
                    sortIndex = propertySortIndex.toLong(),
                    aggregationStyle = property.aggregationStyle,
                    range = property.range,
                    categoryId = categoryId,
                )
                property.units.forEach { unit ->
                    val unitId = unitRepository.create(
                        createdAt = dateTimeFactory.now(),
                        updatedAt = dateTimeFactory.now(),
                        key = unit.key.key,
                        name = localization.getString(unit.name),
                        abbreviation = localization.getString(unit.abbreviation),
                        factor = unit.factor,
                        propertyId = propertyId,
                    )
                    val isSelectedUnit = unit.factor == MeasurementUnit.FACTOR_DEFAULT
                    if (isSelectedUnit) {
                        propertyRepository.update(
                            id = propertyId,
                            updatedAt = dateTimeFactory.now(),
                            name = localization.getString(property.name),
                            sortIndex = propertySortIndex.toLong(),
                            aggregationStyle = property.aggregationStyle,
                            range = property.range,
                            selectedUnitId = unitId,
                        )
                    }
                }
            }
        }

        val foodSeed = seedRepository.getFood()
        foodSeed.forEach { food ->
            foodRepository.create(
                createdAt = dateTimeFactory.now(),
                updatedAt = dateTimeFactory.now(),
                uuid = null,
                name = food.en, // TODO: Localize
                brand = null,
                ingredients = null,
                labels = null, // TODO: Mark seed
                carbohydrates = food.carbohydrates.toDouble(),
                energy = food.energy.toDoubleOrNull(),
                fat = food.fat.toDoubleOrNull(),
                fatSaturated = food.fatSaturated.toDoubleOrNull(),
                fiber = food.fiber.toDoubleOrNull(),
                proteins = food.proteins.toDoubleOrNull(),
                salt = food.salt.toDoubleOrNull(),
                sodium = food.sodium.toDoubleOrNull(),
                sugar = food.sugar.toDoubleOrNull(),
            )
        }

        val tagSeed = seedRepository.getTags()
        tagSeed.forEach { tag ->
            tagRepository.create(
                createdAt = dateTimeFactory.now(),
                updatedAt = dateTimeFactory.now(),
                name = tag.en, // TODO: Localize
            )
        }
    }
}