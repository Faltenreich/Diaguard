package com.faltenreich.diaguard.measurement.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme

@Composable
fun MeasurementCategoryIcon(
    category: MeasurementCategory,
    modifier: Modifier = Modifier,
) {
    val text = category.icon?.takeIf(String::isNotBlank)
        ?: category.name.firstOrNull()?.toString()
        ?: return
    MeasurementCategoryIcon(
        text = text,
        modifier = modifier,
    )
}

@Composable
fun MeasurementCategoryIcon(
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