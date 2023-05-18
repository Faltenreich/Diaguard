package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.property.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun measurementModule() = module {
    singleOf(::MeasurementPropertyRepository)
    singleOf(::MeasurementTypeRepository)
    singleOf(::MeasurementUnitRepository)

    singleOf(::GetMeasurementPropertiesUseCase)
}