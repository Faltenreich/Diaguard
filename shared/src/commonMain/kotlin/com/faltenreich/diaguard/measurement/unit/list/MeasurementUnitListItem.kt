package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun MeasurementUnitListItem(
    unit: MeasurementUnit,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Text(unit.name)
    }
}