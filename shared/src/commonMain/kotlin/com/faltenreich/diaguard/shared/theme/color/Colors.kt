package com.faltenreich.diaguard.shared.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalColors = staticCompositionLocalOf { Colors }

object Colors {

    val Green = Color(0xff478063)
    val Red = Color(0xffdd6050)
    val Yellow = Color(0xffFBC02D)
    val Blue = Color(0xff5771cd)
    val Transparent = Color.Transparent

    val ValueLow = Blue
    val ValueNormal = Green
    val ValueHigh = Red

    val scheme: ColorScheme
        @Composable
        get() = MaterialTheme.colorScheme

    /**
     * Created by Material Theme Builder
     * https://material-foundation.github.io/material-theme-builder
     */

    val primaryLight = Color(0xFF205A40)
    val onPrimaryLight = Color(0xFFFFFFFF)
    val primaryContainerLight = Color(0xFF478063)
    val onPrimaryContainerLight = Color(0xFFFFFFFF)
    val secondaryLight = Color(0xFF24543C)
    val onSecondaryLight = Color(0xFFFFFFFF)
    val secondaryContainerLight = Color(0xFF49795F)
    val onSecondaryContainerLight = Color(0xFFFFFFFF)
    val tertiaryLight = Color(0xFF1A402E)
    val onTertiaryLight = Color(0xFFFFFFFF)
    val tertiaryContainerLight = Color(0xFF3E634F)
    val onTertiaryContainerLight = Color(0xFFFFFFFF)
    val errorLight = Color(0xFF932A1F)
    val onErrorLight = Color(0xFFFFFFFF)
    val errorContainerLight = Color(0xFFC44E3F)
    val onErrorContainerLight = Color(0xFFFFFFFF)
    val backgroundLight = Color(0xFFF8FAF6)
    val onBackgroundLight = Color(0xFF191C1A)
    val surfaceLight = Color(0xFFFCF8F8)
    val onSurfaceLight = Color(0xFF1C1B1B)
    val surfaceVariantLight = Color(0xFFE0E3E3)
    val onSurfaceVariantLight = Color(0xFF444748)
    val outlineLight = Color(0xFF747878)
    val outlineVariantLight = Color(0xFFC4C7C7)
    val scrimLight = Color(0xFF000000)
    val inverseSurfaceLight = Color(0xFF313030)
    val inverseOnSurfaceLight = Color(0xFFF4F0EF)
    val inversePrimaryLight = Color(0xFF98D3B2)
    val surfaceDimLight = Color(0xFFDDD9D9)
    val surfaceBrightLight = Color(0xFFFCF8F8)
    val surfaceContainerLowestLight = Color(0xFFFFFFFF)
    val surfaceContainerLowLight = Color(0xFFF6F3F2)
    val surfaceContainerLight = Color(0xFFF1EDEC)
    val surfaceContainerHighLight = Color(0xFFEBE7E7)
    val surfaceContainerHighestLight = Color(0xFFE5E2E1)

    val primaryDark = Color(0xFF205A40)
    val onPrimaryDark = Color(0xFFFFFFFF)
    val primaryContainerDark = Color(0xFF478063)
    val onPrimaryContainerDark = Color(0xFFFFFFFF)
    val secondaryDark = Color(0xFF24543C)
    val onSecondaryDark = Color(0xFFFFFFFF)
    val secondaryContainerDark = Color(0xFF49795F)
    val onSecondaryContainerDark = Color(0xFFFFFFFF)
    val tertiaryDark = Color(0xFF1A402E)
    val onTertiaryDark = Color(0xFFFFFFFF)
    val tertiaryContainerDark = Color(0xFF3E634F)
    val onTertiaryContainerDark = Color(0xFFFFFFFF)
    val errorDark = Color(0xFFFFB4A9)
    val onErrorDark = Color(0xFF660704)
    val errorContainerDark = Color(0xFFC44E3F)
    val onErrorContainerDark = Color(0xFFFFFFFF)
    val backgroundDark = Color(0xFF111412)
    val onBackgroundDark = Color(0xFFE1E3DF)
    val surfaceDark = Color(0xFF141313)
    val onSurfaceDark = Color(0xFFE5E2E1)
    val surfaceVariantDark = Color(0xFF444748)
    val onSurfaceVariantDark = Color(0xFFC4C7C7)
    val outlineDark = Color(0xFF8E9192)
    val outlineVariantDark = Color(0xFF444748)
    val scrimDark = Color(0xFF000000)
    val inverseSurfaceDark = Color(0xFFE5E2E1)
    val inverseOnSurfaceDark = Color(0xFF313030)
    val inversePrimaryDark = Color(0xFF30694D)
    val surfaceDimDark = Color(0xFF141313)
    val surfaceBrightDark = Color(0xFF3A3939)
    val surfaceContainerLowestDark = Color(0xFF0E0E0E)
    val surfaceContainerLowDark = Color(0xFF1C1B1B)
    val surfaceContainerDark = Color(0xFF201F1F)
    val surfaceContainerHighDark = Color(0xFF2A2A2A)
    val surfaceContainerHighestDark = Color(0xFF353434)
}