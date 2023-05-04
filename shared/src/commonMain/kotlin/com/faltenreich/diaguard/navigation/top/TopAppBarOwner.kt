package com.faltenreich.diaguard.navigation.top

interface TopAppBarOwner {

    val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden
}