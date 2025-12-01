package com.faltenreich.diaguard.measurement.category.list

import com.faltenreich.diaguard.data.measurement.category.MeasurementCategory

data class MeasurementCategoryListState(
    val categories: List<MeasurementCategory.Local>,
    val formDialog: FormDialog?,
) {

    data object FormDialog
}