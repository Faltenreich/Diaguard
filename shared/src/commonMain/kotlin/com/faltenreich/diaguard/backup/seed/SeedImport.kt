package com.faltenreich.diaguard.backup.seed

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.backup.Import
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.serialization.Serialization

class SeedImport(
    private val localization: Localization,
    private val serialization: Serialization,
    private val propertyRepository: MeasurementPropertyRepository,
    private val typeRepository: MeasurementTypeRepository,
    private val unitRepository: MeasurementUnitRepository,
) : Import {

    override operator fun invoke() {
        val yaml = localization.getString(MR.files.properties)
        val seedData = serialization.decodeYaml<List<SeedMeasurementProperty>>(yaml)
        seedData.forEachIndexed { propertySortIndex, property ->
            val propertyId = propertyRepository.create(
                name = property.name.en, // TODO: Determine current locale
                icon = property.icon,
                sortIndex = propertySortIndex.toLong(),
                isUserGenerated = false,
            )
            property.types.forEachIndexed { typeSortIndex, type ->
                val typeId = typeRepository.create(
                    name = type.name.en, // TODO: Determine current locale
                    sortIndex = typeSortIndex.toLong(),
                    propertyId = propertyId,
                )
                type.units.forEach { unit ->
                    val unitId = unitRepository.create(
                        name = unit.name.en, // TODO: Determine current locale
                        factor = unit.factor,
                        typeId = typeId,
                    )
                    if (unit.factor == 1.0) {
                        typeRepository.update(
                            id = typeId,
                            name = type.name.en, // TODO: Determine current locale
                            sortIndex = typeSortIndex.toLong(),
                            selectedUnitId = unitId,
                        )
                    }
                }
            }
        }
    }
}