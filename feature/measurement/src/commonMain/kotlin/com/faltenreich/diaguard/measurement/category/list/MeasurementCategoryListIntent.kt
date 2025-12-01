package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory

sealed interface MeasurementCategoryListIntent {

    data class DecrementSortIndex(val category: MeasurementCategory.Local) : MeasurementCategoryListIntent

    data class IncrementSortIndex(val category: MeasurementCategory.Local) : MeasurementCategoryListIntent

    data class Edit(val category: MeasurementCategory.Local) : MeasurementCategoryListIntent

    data object OpenFormDialog : MeasurementCategoryListIntent

    data object CloseFormDialog : MeasurementCategoryListIntent

    data class Create(val name: String) : MeasurementCategoryListIntent
}