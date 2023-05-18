package com.faltenreich.diaguard.measurement

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun measurementModule() = module {
    singleOf(::MeasurementPropertyRepository)

    singleOf(::GetMeasurementPropertiesUseCase)
}