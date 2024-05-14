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
        categorySeeds.forEachIndexed { categorySortIndex, categorySeed ->
            val category = categoryRepository.create(
                key = categorySeed.key,
                name = localization.getString(categorySeed.name),
                icon = categorySeed.icon,
                sortIndex = categorySortIndex.toLong(),
                isActive = true,
            )
            categorySeed.properties.forEachIndexed { propertySortIndex, propertySeed ->
                val propertyId = propertyRepository.create(
                    createdAt = dateTimeFactory.now(),
                    updatedAt = dateTimeFactory.now(),
                    key = propertySeed.key.key,
                    name = localization.getString(propertySeed.name),
                    sortIndex = propertySortIndex.toLong(),
                    aggregationStyle = propertySeed.aggregationStyle,
                    range = propertySeed.range,
                    categoryId = category.id,
                )
                propertySeed.units.forEach { unitSeed ->
                    val unitId = unitRepository.create(
                        createdAt = dateTimeFactory.now(),
                        updatedAt = dateTimeFactory.now(),
                        key = unitSeed.key.key,
                        name = localization.getString(unitSeed.name),
                        abbreviation = localization.getString(unitSeed.abbreviation),
                        factor = unitSeed.factor,
                        propertyId = propertyId,
                    )
                    val isSelectedUnit = unitSeed.factor == MeasurementUnit.FACTOR_DEFAULT
                    if (isSelectedUnit) {
                        propertyRepository.update(
                            id = propertyId,
                            updatedAt = dateTimeFactory.now(),
                            name = localization.getString(propertySeed.name),
                            sortIndex = propertySortIndex.toLong(),
                            aggregationStyle = propertySeed.aggregationStyle,
                            range = propertySeed.range,
                            selectedUnitId = unitId,
                        )
                    }
                }
            }
        }

        val foodSeeds = seedRepository.getFood()
        foodSeeds.forEach { foodSeed ->
            foodRepository.create(
                createdAt = dateTimeFactory.now(),
                updatedAt = dateTimeFactory.now(),
                uuid = null,
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
        }

        val tagSeeds = seedRepository.getTags()
        tagSeeds.forEach { tagSeed ->
            tagRepository.create(
                createdAt = dateTimeFactory.now(),
                updatedAt = dateTimeFactory.now(),
                name = tagSeed.en, // TODO: Localize
            )
        }
    }
}