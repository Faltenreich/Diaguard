package com.faltenreich.diaguard.view.bar

import androidx.compose.runtime.Composable

sealed interface BottomAppBarStyle {

    data class Visible(
        val actions: @Composable () -> Unit = {},
        val floatingActionButton: @Composable () -> Unit = {},
    ) : BottomAppBarStyle
}