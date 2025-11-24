package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable

interface Screen {

    @Composable
    fun TopAppBar(): com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle = _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.Hidden

    @Composable
    fun BottomAppBar(): com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle = _root_ide_package_.com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle.Visible()

    @Composable
    fun Content()
}