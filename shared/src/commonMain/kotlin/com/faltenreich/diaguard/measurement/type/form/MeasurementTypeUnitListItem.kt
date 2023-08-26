package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun MeasurementTypeUnitListItem(
    typeUnit: MeasurementTypeUnit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { /* TODO */ }) {
            Text(
                text = typeUnit.unit.name,
                modifier = Modifier.weight(1f),
            )
            Text(typeUnit.factor.toString()) // TODO: Format
        }
        Divider()
    }
}