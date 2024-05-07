package com.faltenreich.diaguard.measurement.category.form

sealed interface MeasurementCategoryFormIntent {

    data object OpenIconPicker : MeasurementCategoryFormIntent

    data object Confirm : MeasurementCategoryFormIntent

    data object DeleteCategory : MeasurementCategoryFormIntent
}