package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable

sealed class BottomAppBarStyle {

    object Hidden : BottomAppBarStyle()

    data class Visible(
        val actions: @Composable () -> Unit = {},
        val floatingActionButton: @Composable () -> Unit = {},
    ) : BottomAppBarStyle()
}