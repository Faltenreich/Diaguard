package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.category.form.CountMeasurementValuesOfCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.CreateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.category.form.DeleteMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.GetMeasurementTypesUseCase
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormViewModel
import com.faltenreich.diaguard.measurement.category.form.UpdateMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.list.CreateMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.list.GetMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListViewModel
import com.faltenreich.diaguard.measurement.type.MeasurementType
import com.faltenreich.diaguard.measurement.type.MeasurementTypeRepository
import com.faltenreich.diaguard.measurement.type.form.CountMeasurementValuesOfTypeUseCase
import com.faltenreich.diaguard.measurement.type.form.DeleteMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.type.form.GetMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewModel
import com.faltenreich.diaguard.measurement.type.form.UpdateMeasurementTypeUseCase
import com.faltenreich.diaguard.measurement.type.list.MeasurementTypeListViewModel
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.unit.UpdateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListViewModel
import com.faltenreich.diaguard.measurement.value.CreateMeasurementValuesUseCase
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun measurementModule() = module {
    singleOf(::MeasurementValueMapper)

    singleOf(::MeasurementCategoryRepository)
    singleOf(::MeasurementTypeRepository)
    singleOf(::MeasurementUnitRepository)
    singleOf(::MeasurementValueRepository)

    singleOf(::CreateMeasurementCategoryUseCase)
    singleOf(::GetMeasurementValueTintUseCase)
    singleOf(::GetMeasurementCategoriesUseCase)
    singleOf(::UpdateMeasurementCategoryUseCase)
    singleOf(::DeleteMeasurementCategoryUseCase)
    singleOf(::CreateMeasurementTypeUseCase)
    singleOf(::GetMeasurementTypesUseCase)
    singleOf(::GetMeasurementTypeUseCase)
    singleOf(::CreateMeasurementValuesUseCase)
    singleOf(::CountMeasurementValuesOfCategoryUseCase)
    singleOf(::CountMeasurementValuesOfTypeUseCase)
    singleOf(::UpdateMeasurementTypeUseCase)
    singleOf(::DeleteMeasurementTypeUseCase)
    singleOf(::UpdateMeasurementUnitUseCase)

    singleOf(::MeasurementCategoryListViewModel)
    factory { (category: MeasurementCategory) -> MeasurementCategoryFormViewModel(category) }
    singleOf(::MeasurementTypeListViewModel)
    factory { (type: MeasurementType) -> MeasurementTypeFormViewModel(type) }
    singleOf(::MeasurementUnitListViewModel)
}