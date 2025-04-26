package com.faltenreich.diaguard.measurement

import com.faltenreich.diaguard.measurement.category.GetActiveMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.GetAllMeasurementCategoriesUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.category.StoreMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.DeleteMeasurementCategoryUseCase
import com.faltenreich.diaguard.measurement.category.form.GetMeasurementCategoryByIdUseCase
import com.faltenreich.diaguard.measurement.category.form.GetMeasurementPropertiesUseCase
import com.faltenreich.diaguard.measurement.category.form.MeasurementCategoryFormViewModel
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListViewModel
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyRepository
import com.faltenreich.diaguard.measurement.property.StoreMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.form.CountMeasurementValuesOfPropertyUseCase
import com.faltenreich.diaguard.measurement.property.form.DeleteMeasurementPropertyUseCase
import com.faltenreich.diaguard.measurement.property.form.GetMaximumSortIndexUseCase
import com.faltenreich.diaguard.measurement.property.form.GetMeasurementPropertyBdIdUseCase
import com.faltenreich.diaguard.measurement.property.form.GetMeasurementUnitSuggestionsUseCase
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormStateFactory
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.measurement.unit.MeasurementUnitRepository
import com.faltenreich.diaguard.measurement.unit.StoreMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.UniqueMeasurementUnitRule
import com.faltenreich.diaguard.measurement.unit.ValidateMeasurementUnitUseCase
import com.faltenreich.diaguard.measurement.unit.list.GetMeasurementUnitsUseCase
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListMode
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListViewModel
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitSelectionViewModel
import com.faltenreich.diaguard.measurement.unit.suggestion.MeasurementUnitSuggestionRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueMapper
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.measurement.value.StoreMeasurementValuesUseCase
import com.faltenreich.diaguard.measurement.value.tint.GetMeasurementValueTintUseCase
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
    factoryOf(::GetAllMeasurementCategoriesUseCase)
    factoryOf(::GetMeasurementCategoryByIdUseCase)
    factoryOf(::DeleteMeasurementCategoryUseCase)
    factoryOf(::MeasurementPropertyFormStateFactory)
    factoryOf(::StoreMeasurementPropertyUseCase)
    factoryOf(::GetMaximumSortIndexUseCase)
    factoryOf(::GetMeasurementUnitsUseCase)
    factoryOf(::GetMeasurementPropertyBdIdUseCase)
    factoryOf(::GetMeasurementUnitSuggestionsUseCase)
    factoryOf(::GetMeasurementPropertiesUseCase)
    factoryOf(::StoreMeasurementValuesUseCase)
    factoryOf(::CountMeasurementValuesOfPropertyUseCase)
    factoryOf(::DeleteMeasurementPropertyUseCase)
    factory { ValidateMeasurementUnitUseCase(rules = listOf(UniqueMeasurementUnitRule())) }
    factoryOf(::StoreMeasurementUnitUseCase)

    viewModelOf(::MeasurementCategoryListViewModel)
    viewModel { (categoryId: Long) -> MeasurementCategoryFormViewModel(categoryId) }
    viewModel { (categoryId: Long, propertyId: Long?) -> MeasurementPropertyFormViewModel(categoryId, propertyId) }
    viewModel { (mode: MeasurementUnitListMode) -> MeasurementUnitListViewModel(mode) }
    viewModelOf(::MeasurementUnitSelectionViewModel)
}