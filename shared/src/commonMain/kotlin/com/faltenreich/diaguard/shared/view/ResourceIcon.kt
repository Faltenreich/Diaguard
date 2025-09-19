package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_add
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
private fun Preview() = AppPreview {
    ResourceIcon(icon = Res.drawable.ic_add)
}