package com.faltenreich.diaguard.measurement.property

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun MeasurementPropertyIcon(
    property: MeasurementProperty,
    modifier: Modifier = Modifier,
) {
    val text = property.icon?.takeIf(String::isNotBlank)
        ?: property.name.firstOrNull()?.toString()
        ?: return
    MeasurementPropertyIcon(
        text = text,
        modifier = modifier,
    )
}

@Composable
fun MeasurementPropertyIcon(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = AppTheme.typography.headlineSmall,
        )
    }
}