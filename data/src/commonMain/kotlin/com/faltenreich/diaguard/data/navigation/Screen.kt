package com.faltenreich.diaguard.data.navigation

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle

interface Screen {

    @Composable
    fun TopAppBar(): TopAppBarStyle = TopAppBarStyle.Hidden

    @Composable
    fun BottomAppBar(): BottomAppBarStyle = BottomAppBarStyle.Visible()

    @Composable
    fun Content()
}