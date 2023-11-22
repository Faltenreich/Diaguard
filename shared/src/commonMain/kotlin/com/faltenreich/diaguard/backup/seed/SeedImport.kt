package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.food.FoodRepository
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.localization.Localization

class SeedImport(
    private val localization: Localization,
    private val dateTimeFactory: DateTimeFactory,
    private val seedRepository: SeedRepository,
    private val propertyRepository: MeasurementPropertyRepository,
    private val typeRepository: MeasurementTypeRepository,
    private val unitRepository: MeasurementUnitRepository,
    private val foodRepository: FoodRepository,
) : Import {

    override fun import() {
        val now = dateTimeFactory.now()

        val propertySeeds = seedRepository.getMeasurementProperties()
        propertySeeds.forEachIndexed { propertySortIndex, property ->
            val propertyId = propertyRepository.create(
                key = property.key.key,
                name = localization.getString(property.name),
                icon = property.icon,
                sortIndex = propertySortIndex.toLong(),
            )
            property.types.forEachIndexed { typeSortIndex, type ->
                val typeId = typeRepository.create(
                    key = type.key.key,
                    name = localization.getString(type.name),
                    sortIndex = typeSortIndex.toLong(),
                    propertyId = propertyId,
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
    }
}