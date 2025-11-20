package com.faltenreich.diaguard.view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.view.theme.AppTheme
import diaguard.core.view.generated.resources.Res
import diaguard.core.view.generated.resources.ic_add
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ResourceIcon(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = AppTheme.colors.scheme.onBackground.copy(
        alpha = LocalContentColor.current.alpha,
    ),
) {
    Icon(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
        tint = tint,
    )
}

@Preview
@Composable
private fun Preview() {
    ResourceIcon(icon = Res.drawable.ic_add)
}