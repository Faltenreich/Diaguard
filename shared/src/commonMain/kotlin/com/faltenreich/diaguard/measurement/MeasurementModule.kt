package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormViewModel
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListViewModel
import com.faltenreich.diaguard.measurement.category.usecase.DeleteMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.usecase.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.usecase.GetMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.usecase.GetMeasurementCategoryByIdUseCase
import com.faltenreich.diaguard.measurement.category.usecase.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.data.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormStateFactory
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.measurement.property.usecase.DeleteMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.usecase.GetMaximumSortIndexOfMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.usecase.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.property.usecase.GetMeasurementPropertyBdIdUseCase
import com.faltenreich.diaguard.measurement.property.usecase.StoreMeasurementPropertyUseCase
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.unit.UniqueMeasurementUnitRule
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListMode
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListViewModel
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionViewModel
import com.faltenreich.diaguard.data.measurement.unit.suggestion.MeasurementUnitSuggestionRepository
import com.faltenreich.diaguard.measurement.unit.usecase.GetMeasurementUnitSuggestionsUseCase
import com.faltenreich.diaguard.measurement.unit.usecase.GetMeasurementUnitsUseCase
import com.faltenreich.diaguard.measurement.unit.usecase.StoreMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.usecase.ValidateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.data.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.usecase.GetMeasurementValueTintUseCase
import com.faltenreich.diaguard.measurement.value.usecase.StoreMeasurementValuesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun measurementModule() = module {
    factoryOf(::MeasurementValueMapper)

    factoryOf(::MeasurementCategoryRepository)
    factoryOf(::MeasurementPropertyRepository)
    factoryOf(::MeasurementUnitRepository)
    factoryOf(::MeasurementUnitSuggestionRepository)
    factoryOf(::MeasurementValueRepository)

    factoryOf(::StoreMeasurementCategoryUseCase)
    factoryOf(::GetMeasurementValueTintUseCase)
    factoryOf(::GetActiveMeasurementCategoriesUseCase)
    factoryOf(::GetMeasurementCategoriesUseCase)
    factoryOf(::GetMeasurementCategoryByIdUseCase)
    factoryOf(::DeleteMeasurementCategoryUseCase)
    factoryOf(::MeasurementPropertyFormStateFactory)
    factoryOf(::StoreMeasurementPropertyUseCase)
    factoryOf(::GetMaximumSortIndexOfMeasurementPropertyUseCase)
    factoryOf(::GetMeasurementUnitsUseCase)
    factoryOf(::GetMeasurementPropertyBdIdUseCase)
    factoryOf(::GetMeasurementUnitSuggestionsUseCase)
    factoryOf(::GetMeasurementPropertiesUseCase)
    factoryOf(::StoreMeasurementValuesUseCase)
    factoryOf(::DeleteMeasurementPropertyUseCase)
    factory { ValidateMeasurementUnitUseCase(rules = listOf(UniqueMeasurementUnitRule())) }
    factoryOf(::StoreMeasurementUnitUseCase)

    viewModelOf(::MeasurementCategoryListViewModel)
    viewModel { (categoryId: Long) -> MeasurementCategoryFormViewModel(categoryId) }
    viewModel { (categoryId: Long, propertyId: Long?) -> MeasurementPropertyFormViewModel(categoryId, propertyId) }
    viewModel { (mode: MeasurementUnitListMode) -> MeasurementUnitListViewModel(mode) }
    viewModelOf(::MeasurementUnitSelectionViewModel)
}