package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.datetime.factory.DateTimeFactory
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.tag.TagRepository

class SeedImport(
    private val localization: Localization,
    private val dateTimeFactory: DateTimeFactory,
    private val seedRepository: SeedRepository,
    private val categoryRepository: MeasurementCategoryRepository,
    private val typeRepository: MeasurementTypeRepository,
    private val unitRepository: MeasurementUnitRepository,
    private val foodRepository: FoodRepository,
    private val tagRepository: TagRepository,
) : Import {

    override fun import() {
        val now = dateTimeFactory.now()

        val categorySeeds = seedRepository.getMeasurementCategories()
        categorySeeds.forEachIndexed { categorySortIndex, category ->
            val categoryId = categoryRepository.create(
                key = category.key.key,
                name = localization.getString(category.name),
                icon = category.icon,
                sortIndex = categorySortIndex.toLong(),
            )
            category.types.forEachIndexed { typeSortIndex, type ->
                val typeId = typeRepository.create(
                    key = type.key.key,
                    name = localization.getString(type.name),
                    sortIndex = typeSortIndex.toLong(),
                    range = type.range,
                    categoryId = categoryId,
                )
                type.units.forEach { unit ->
                    val unitId = unitRepository.create(
                        key = unit.key.key,
                        name = localization.getString(unit.name),
                        abbreviation = localization.getString(unit.abbreviation),
                        factor = unit.factor,
                        typeId = typeId,
                    )
                    val isSelectedUnit = unit.factor == MeasurementUnit.FACTOR_DEFAULT
                    if (isSelectedUnit) {
                        typeRepository.update(
                            id = typeId,
                            name = localization.getString(type.name),
                            range = type.range,
                            sortIndex = typeSortIndex.toLong(),
                            selectedUnitId = unitId,
                        )
                    }
                }
            }
        }

        val foodSeed = seedRepository.getFood()
        foodSeed.forEach { food ->
            foodRepository.create(
                createdAt = now,
                updatedAt = now,
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
                createdAt = now,
                updatedAt = now,
                name = tag.en, // TODO: Localize
            )
        }
    }
}