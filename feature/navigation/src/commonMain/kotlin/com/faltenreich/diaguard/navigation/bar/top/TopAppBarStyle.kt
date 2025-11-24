package com.faltenreich.diaguard.navigation.bar.top

import androidx.compose.runtime.Composable

sealed interface TopAppBarStyle {

    data object Hidden : com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle

    data class CenterAligned(val content: @Composable () -> Unit) :
        com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle

    data class Custom(val content: @Composable () -> Unit) :
        com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
}