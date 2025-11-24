package com.faltenreich.diaguard.navigation.bar.bottom

import androidx.compose.runtime.Composable

sealed interface BottomAppBarStyle {

    data class Visible(
        val actions: @Composable () -> Unit = {},
        val floatingActionButton: @Composable () -> Unit = {},
    ) : BottomAppBarStyle
}