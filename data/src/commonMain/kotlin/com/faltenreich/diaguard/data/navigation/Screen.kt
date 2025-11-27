package com.faltenreich.diaguard.data.navigation

import androidx.compose.runtime.Composable

interface Screen {

    @Composable
    fun TopAppBar(): TopAppBarStyle = TopAppBarStyle.Hidden

    @Composable
    fun BottomAppBar(): BottomAppBarStyle = BottomAppBarStyle.Visible()

    @Composable
    fun Content()
}