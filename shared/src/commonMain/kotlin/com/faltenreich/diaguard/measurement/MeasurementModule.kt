package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.form.CountMeasurementValuesOfPropertyUseCase
import com.faltenreich.diaguard.measurement.property.form.CreateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.property.form.DeleteMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.form.GetMeasurementTypesUseCase
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.list.CreateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.list.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListViewModel
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.type.form.CountMeasurementValuesOfTypeUseCase
import com.faltenreich.diaguard.measurement.type.form.DeleteMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.type.form.GetMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewModel
import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.type.list.MeasurementTypeListViewModel
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListViewModel
import com.faltenreich.diaguard.measurement.value.CreateMeasurementValuesUseCase
import com.faltenreich.diaguard.measurement.value.GetMeasurementValueColorUseCase
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun measurementModule() = module {
    singleOf(::MeasurementValueMapper)

    singleOf(::MeasurementPropertyRepository)
    singleOf(::MeasurementTypeRepository)
    singleOf(::MeasurementUnitRepository)
    singleOf(::MeasurementValueRepository)

    singleOf(::CreateMeasurementPropertyUseCase)
    singleOf(::GetMeasurementValueColorUseCase)
    singleOf(::GetMeasurementPropertiesUseCase)
    singleOf(::UpdateMeasurementPropertyUseCase)
    singleOf(::DeleteMeasurementPropertyUseCase)
    singleOf(::CreateMeasurementTypeUseCase)
    singleOf(::GetMeasurementTypesUseCase)
    singleOf(::GetMeasurementTypeUseCase)
    singleOf(::CreateMeasurementValuesUseCase)
    singleOf(::CountMeasurementValuesOfPropertyUseCase)
    singleOf(::CountMeasurementValuesOfTypeUseCase)
    singleOf(::UpdateMeasurementTypeUseCase)
    singleOf(::DeleteMeasurementTypeUseCase)

    singleOf(::MeasurementPropertyListViewModel)
    factory { (property: MeasurementProperty) -> MeasurementPropertyFormViewModel(property) }
    singleOf(::MeasurementTypeListViewModel)
    factory { (measurementType: MeasurementType) -> MeasurementTypeFormViewModel(measurementType) }
    singleOf(::MeasurementUnitListViewModel)
}