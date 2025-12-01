package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.data.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.data.preference.color.ColorScheme

data class MeasurementCategoryFormState(
    val name: String,
    val icon: String?,
    val isActive: Boolean,
    val properties: List<MeasurementProperty.Local>,
    val colorScheme: ColorScheme,
    val deleteDialog: DeleteDialog?,
    val alertDialog: AlertDialog?,
) {

    data object DeleteDialog

    data object AlertDialog
}