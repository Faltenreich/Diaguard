package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.MR
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


    fun getData(): List<SeedMeasurementProperty> {
        return properties {
            property {
                key = "blood_sugar"
                name = MR.strings.blood_sugar
                icon = ""
                type {
                    key = "blood_sugar"
                    name = MR.strings.blood_sugar
                    unit {
                        key = "mg_dl"
                        name = MR.strings.milligrams_per_deciliter
                        abbreviation = MR.strings.milligrams_per_deciliter_abbreviation
                        factor = 1.0
                    }
                }
            }
        }
    }

    override fun import() {
        val seed = getData()
        seed.forEachIndexed { propertySortIndex, property ->
            val propertyId = propertyRepository.create(
                key = property.key,
                name = localization.getString(property.name),
                icon = property.icon,
                sortIndex = propertySortIndex.toLong(),
            )
            property.types.forEachIndexed { typeSortIndex, type ->
                val typeId = typeRepository.create(
                    key = type.key,
                    name = localization.getString(type.name),
                    sortIndex = typeSortIndex.toLong(),
                    propertyId = propertyId,
                )
                type.units.forEach { unit ->
                    val unitId = unitRepository.create(
                        key = unit.key,
                        name = localization.getString(unit.name),
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
    }
}