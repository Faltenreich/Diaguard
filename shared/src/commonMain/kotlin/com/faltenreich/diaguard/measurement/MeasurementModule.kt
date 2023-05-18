package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.property.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun measurementModule() = module {
    singleOf(::MeasurementPropertyRepository)

    singleOf(::GetMeasurementPropertiesUseCase)
}