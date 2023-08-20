package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun ResourceIcon(
    imageResource: ImageResource,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(imageResource),
        contentDescription = null,
        modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
        tint = AppTheme.colors.material.onBackground,
    )
}