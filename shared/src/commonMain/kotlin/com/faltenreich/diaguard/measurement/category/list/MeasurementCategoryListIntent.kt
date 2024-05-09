package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.measurement.category.MeasurementCategory

sealed interface MeasurementCategoryListIntent {

    data class ChangeIsActive(val category: MeasurementCategory) : MeasurementCategoryListIntent

    data class DecrementSortIndex(val category: MeasurementCategory) : MeasurementCategoryListIntent

    data class IncrementSortIndex(val category: MeasurementCategory) : MeasurementCategoryListIntent

    data class Edit(val category: MeasurementCategory) : MeasurementCategoryListIntent

    data object Create : MeasurementCategoryListIntent
}