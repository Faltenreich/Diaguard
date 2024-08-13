package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle

interface Screen {

    val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible()

    @Composable
    fun TopAppBar(): TopAppBarStyle = TopAppBarStyle.Hidden

    @Composable
    fun BottomAppBar(): BottomAppBarStyle = BottomAppBarStyle.Hidden

    @Composable
    fun Content()
}