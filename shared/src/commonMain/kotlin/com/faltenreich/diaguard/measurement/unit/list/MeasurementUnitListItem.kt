package com.faltenreich.diaguard.measurement.unit.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.view.layout.FormRow
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementUnitListItem(
    unit: MeasurementUnit,
    modifier: Modifier = Modifier,
) {
    FormRow(modifier = modifier) {
        Text(unit.name)
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    MeasurementUnitListItem(unit = unit())
}