package com.faltenreich.diaguard.measurement.property.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.measurement.property.MeasurementProperty

@Composable
fun MeasurementPropertyListItem(
    property: MeasurementProperty,
    modifier: Modifier = Modifier,
) {
    Text(property.name)
}