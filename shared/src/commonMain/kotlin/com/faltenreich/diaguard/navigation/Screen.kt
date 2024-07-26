package com.faltenreich.diaguard.navigation

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle

interface Screen {

    val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible()

    @Deprecated("Use NavGraphBuilder instead")
    @Composable
    fun Content() = Unit
}