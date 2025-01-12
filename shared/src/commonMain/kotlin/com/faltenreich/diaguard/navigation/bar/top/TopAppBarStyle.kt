package com.faltenreich.diaguard.navigation.bar.top

import androidx.compose.runtime.Composable

sealed interface TopAppBarStyle {

    // Dark by default as content should be white, e.g. on TopAppBar with green primary color
    val statusBarStyle: StatusBarStyle
        get() = StatusBarStyle.Dark

    data class Hidden(
        override val statusBarStyle: StatusBarStyle = StatusBarStyle.Dark,
    ) : TopAppBarStyle

    data class CenterAligned(val content: @Composable () -> Unit) : TopAppBarStyle

    data class Custom(val content: @Composable () -> Unit) : TopAppBarStyle
}