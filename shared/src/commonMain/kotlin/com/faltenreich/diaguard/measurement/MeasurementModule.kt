package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnitRepository
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun measurementModule() = module {
    singleOf(::MeasurementValueFormatter)

    singleOf(::MeasurementPropertyRepository)
    singleOf(::MeasurementTypeRepository)
    singleOf(::MeasurementUnitRepository)
    singleOf(::MeasurementTypeUnitRepository)
    singleOf(::MeasurementValueRepository)
}