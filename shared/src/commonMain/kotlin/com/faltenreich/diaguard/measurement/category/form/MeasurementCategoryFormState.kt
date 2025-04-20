package com.faltenreich.diaguard.measurement.category.form

import com.faltenreich.diaguard.measurement.property.MeasurementProperty

data class MeasurementCategoryFormState(
    val properties: List<MeasurementProperty.Local>,
    val deleteDialog: DeleteDialog?,
    val alertDialog: AlertDialog?,
) {

    data object DeleteDialog

    data object AlertDialog
}