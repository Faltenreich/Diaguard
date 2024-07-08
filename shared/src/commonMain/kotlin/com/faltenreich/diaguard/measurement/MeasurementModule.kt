package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.GetAllMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.category.form.CountMeasurementValuesOfCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.CreateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.category.form.DeleteMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormViewModel
import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.list.CreateMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListViewModel
import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.form.CountMeasurementValuesOfPropertyUseCase
import com.faltenreich.diaguard.measurement.property.form.DeleteMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.measurement.property.form.UpdateMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.list.MeasurementPropertyListViewModel
import com.faltenreich.diaguard.measurement.unit.GetMeasurementUnitsOfPropertyUseCase
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.unit.UpdateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.value.CreateMeasurementValuesUseCase
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun measurementModule() = module {
    singleOf(::MeasurementValueMapper)

    singleOf(::MeasurementCategoryRepository)
    singleOf(::MeasurementPropertyRepository)
    singleOf(::MeasurementUnitRepository)
    singleOf(::MeasurementValueRepository)

    singleOf(::CreateMeasurementCategoryUseCase)
    singleOf(::GetMeasurementValueTintUseCase)
    singleOf(::GetActiveMeasurementCategoriesUseCase)
    singleOf(::GetAllMeasurementCategoriesUseCase)
    singleOf(::UpdateMeasurementCategoryUseCase)
    singleOf(::DeleteMeasurementCategoryUseCase)
    singleOf(::CreateMeasurementPropertyUseCase)
    singleOf(::GetMeasurementUnitsOfPropertyUseCase)
    singleOf(::GetMeasurementPropertiesUseCase)
    singleOf(::CreateMeasurementValuesUseCase)
    singleOf(::CountMeasurementValuesOfCategoryUseCase)
    singleOf(::CountMeasurementValuesOfPropertyUseCase)
    singleOf(::UpdateMeasurementPropertyUseCase)
    singleOf(::DeleteMeasurementPropertyUseCase)
    singleOf(::UpdateMeasurementUnitUseCase)

    factoryOf(::MeasurementCategoryListViewModel)
    factory { (category: MeasurementCategory.Local) -> MeasurementCategoryFormViewModel(category) }
    factoryOf(::MeasurementPropertyListViewModel)
    factory { (property: MeasurementProperty.Local) -> MeasurementPropertyFormViewModel(property) }
}