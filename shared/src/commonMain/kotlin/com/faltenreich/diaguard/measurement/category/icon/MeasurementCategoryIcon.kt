package com.faltenreich.diaguard.measurement.category.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.measurement_category_icon_default
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementCategoryIcon(
    category: MeasurementCategory,
    modifier: Modifier = Modifier,
) {
    MeasurementCategoryIcon(
        icon = category.icon,
        fallback = category.name,
        modifier = modifier,
    )
}

@Composable
fun MeasurementCategoryIcon(
    icon: String?,
    fallback: String,
    modifier: Modifier = Modifier,
) {
    val text = icon
        ?: fallback.firstOrNull()?.uppercase()
        ?: stringResource(Res.string.measurement_category_icon_default)
    Box(
        modifier = modifier
            .size(AppTheme.dimensions.size.ImageMedium)
            .background(
                // TODO: Generate color from "fallback"
                color = if (icon != null) Color.Transparent else AppTheme.colors.scheme.onSurface,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = AppTheme.typography.headlineSmall,
            color = AppTheme.colors.scheme.inverseOnSurface,
        )
    }
}