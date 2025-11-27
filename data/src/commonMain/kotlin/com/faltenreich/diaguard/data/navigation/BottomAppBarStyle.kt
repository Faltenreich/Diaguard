package com.faltenreich.diaguard.data.navigation

import androidx.compose.runtime.Composable

sealed interface BottomAppBarStyle {

    data class Visible(
        val actions: @Composable () -> Unit = {},
        val floatingActionButton: @Composable () -> Unit = {},
    ) : BottomAppBarStyle
}