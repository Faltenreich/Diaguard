package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty
import com.faltenreich.diaguard.preference.color.ColorScheme

data class MeasurementCategoryFormState(
    val properties: List<MeasurementProperty.Local>,
    val colorScheme: ColorScheme,
    val deleteDialog: DeleteDialog?,
    val alertDialog: AlertDialog?,
) {

    data object DeleteDialog

    data object AlertDialog
}