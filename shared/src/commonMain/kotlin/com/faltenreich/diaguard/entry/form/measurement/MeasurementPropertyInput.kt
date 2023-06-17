package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun MeasurementPropertyInput(
    data: MeasurementPropertyInputData,
    modifier: Modifier = Modifier,
    onInputChange: (MeasurementTypeInputData) -> Unit,
) {
    FormRow(
        icon = { MeasurementPropertyIcon(data.property) },
        modifier = modifier,
    ) {
        Column {
            data.typeInputDataList.forEach { typeInputData ->
                MeasurementTypeInput(
                    data = typeInputData,
                    onInputChange = onInputChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}