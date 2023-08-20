package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.measurement.property.list.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListViewModel
import com.faltenreich.diaguard.measurement.property.list.SetMeasurementPropertySortIndexUseCase
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.type.list.GetMeasurementTypesUseCase
import com.faltenreich.diaguard.measurement.type.list.MeasurementTypeListViewModel
import com.faltenreich.diaguard.measurement.type.list.SetMeasurementTypeSortIndexUseCase
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

    singleOf(::GetMeasurementPropertiesUseCase)
    singleOf(::SetMeasurementPropertySortIndexUseCase)
    singleOf(::MeasurementPropertyListViewModel)

    factory { (property: MeasurementProperty) -> MeasurementPropertyFormViewModel(property) }

    singleOf(::GetMeasurementTypesUseCase)
    singleOf(::SetMeasurementTypeSortIndexUseCase)
    factory { (property: MeasurementProperty) -> MeasurementTypeListViewModel(property) }
}