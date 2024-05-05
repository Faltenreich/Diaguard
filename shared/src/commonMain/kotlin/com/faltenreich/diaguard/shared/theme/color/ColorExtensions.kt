package com.faltenreich.diaguard.shared.theme.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private const val ANIMATION_DURATION_MILLIS_DEFAULT = 500

@Composable
fun Color.animated(
    spec: AnimationSpec<Color> = tween(durationMillis = ANIMATION_DURATION_MILLIS_DEFAULT),
): Color {
    return animateColorAsState(
        targetValue = this,
        animationSpec = spec,
    ).value
}

@Composable
fun ColorScheme.animated(): ColorScheme = copy(
    primary = primary.animated(),
    onPrimary = onPrimary.animated(),
    primaryContainer = primaryContainer.animated(),
    onPrimaryContainer = onPrimaryContainer.animated(),
    inversePrimary = inversePrimary.animated(),
    secondary = secondary.animated(),
    onSecondary = onSecondary.animated(),
    secondaryContainer = secondaryContainer.animated(),
    onSecondaryContainer = onSecondaryContainer.animated(),
    tertiary = tertiary.animated(),
    onTertiary = onTertiary.animated(),
    tertiaryContainer = tertiaryContainer.animated(),
    onTertiaryContainer = onTertiaryContainer.animated(),
    background = background.animated(),
    onBackground = onBackground.animated(),
    surface = surface.animated(),
    onSurface = onSurface.animated(),
    surfaceVariant = surfaceVariant.animated(),
    onSurfaceVariant = onSurfaceVariant.animated(),
    surfaceTint = surfaceTint.animated(),
    inverseSurface = inverseSurface.animated(),
    inverseOnSurface = inverseOnSurface.animated(),
    error = error.animated(),
    onError = onError.animated(),
    errorContainer = errorContainer.animated(),
    onErrorContainer = onErrorContainer.animated(),
    outline = outline.animated(),
    outlineVariant = outlineVariant.animated(),
    scrim = scrim.animated(),
)

fun Char.asColor(): Color {
    val hue = ((hashCode() shl 5) - hashCode()) % 360
    return Color.hsl(
        hue = hue.toFloat(),
        saturation = .8f,
        lightness = .4f,
    )
}