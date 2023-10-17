package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.localization.Localization

class SeedImport(
    private val localization: Localization,
    private val propertyRepository: MeasurementPropertyRepository,
    private val typeRepository: MeasurementTypeRepository,
    private val unitRepository: MeasurementUnitRepository,
) : Import {

    override fun import() {
        val seed = SeedMeasurementProperty.entries
        seed.forEachIndexed { propertySortIndex, property ->
            val propertyId = propertyRepository.create(
                key = property.key,
                name = localization.getString(property.localization),
                icon = property.icon,
                sortIndex = propertySortIndex.toLong(),
            )
            property.types.forEachIndexed { typeSortIndex, type ->
                val typeId = typeRepository.create(
                    key = type.key,
                    name = localization.getString(type.localization),
                    sortIndex = typeSortIndex.toLong(),
                    propertyId = propertyId,
                )
                type.unitsWithFactor.forEach { (unit, factor) ->
                    val unitId = unitRepository.create(
                        key = unit.key,
                        name = localization.getString(unit.localization),
                        factor = factor,
                        typeId = typeId,
                    )
                    val isSelectedUnit = factor == MeasurementUnit.FACTOR_DEFAULT
                    if (isSelectedUnit) {
                        typeRepository.update(
                            id = typeId,
                            name = localization.getString(type.localization),
                            sortIndex = typeSortIndex.toLong(),
                            selectedUnitId = unitId,
                        )
                    }
                }
            }
        }
    }
}