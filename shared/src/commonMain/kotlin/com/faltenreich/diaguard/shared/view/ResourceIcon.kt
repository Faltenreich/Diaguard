package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ResourceIcon(
    icon: DrawableResource,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
        tint = AppTheme.colors.scheme.onBackground,
    )
}