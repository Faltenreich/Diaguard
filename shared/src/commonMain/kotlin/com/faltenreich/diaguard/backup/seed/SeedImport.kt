package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.backup.seed.property.ActivitySeed
import com.faltenreich.diaguard.backup.seed.property.BloodPressureSeed
import com.faltenreich.diaguard.backup.seed.property.BloodSugarSeed
import com.faltenreich.diaguard.backup.seed.property.HbA1cSeed
import com.faltenreich.diaguard.backup.seed.property.InsulinSeed
import com.faltenreich.diaguard.backup.seed.property.MealSeed
import com.faltenreich.diaguard.backup.seed.property.OxygenSaturationSeed
import com.faltenreich.diaguard.backup.seed.property.PulseSeed
import com.faltenreich.diaguard.backup.seed.property.WeightSeed
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
        val seeds = listOf(
            BloodSugarSeed(),
            InsulinSeed(),
            MealSeed(),
            ActivitySeed(),
            HbA1cSeed(),
            WeightSeed(),
            PulseSeed(),
            BloodPressureSeed(),
            OxygenSaturationSeed(),
        )
        seeds.forEachIndexed { propertySortIndex, seed ->
            val property = seed.harvest()
            val propertyId = propertyRepository.create(
                key = property.key,
                name = localization.getString(property.name),
                icon = property.icon,
                sortIndex = propertySortIndex.toLong(),
            )
            property.types.forEachIndexed { typeSortIndex, type ->
                val typeId = typeRepository.create(
                    key = "${property.key}.${type.key}",
                    name = localization.getString(type.name),
                    sortIndex = typeSortIndex.toLong(),
                    propertyId = propertyId,
                )
                type.units.forEach { unit ->
                    val unitId = unitRepository.create(
                        key = "${property.key}.${type.key}.${unit.key}",
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