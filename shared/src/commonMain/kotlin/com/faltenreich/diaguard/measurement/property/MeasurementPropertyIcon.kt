package com.faltenreich.diaguard.measurement.property

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MeasurementPropertyIcon(
    property: MeasurementProperty,
    modifier: Modifier = Modifier,
) {
    val text = property.icon ?: property.name.firstOrNull()?.toString() ?: return
    Text(
        text = text,
        modifier = modifier,
    )
}