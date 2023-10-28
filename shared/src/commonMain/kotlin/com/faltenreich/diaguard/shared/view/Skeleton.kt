package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.valentinilk.shimmer.shimmer

@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(AppTheme.colors.scheme.error),
    )
}

@Composable
fun <T> Skeleton(
    item: T?,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    Box(modifier = modifier.skeleton(show = item == null)) {
        if (item != null) {
            content(item)
        }
    }
}

@Composable
fun Modifier.skeleton(show: Boolean): Modifier {
    return if (show) {
        fillMaxHeight()
            .defaultMinSize(minWidth = AppTheme.dimensions.size.TouchSizeLarge)
            .shimmer()
            .background(AppTheme.colors.scheme.surfaceVariant)
    } else {
        this
    }
}