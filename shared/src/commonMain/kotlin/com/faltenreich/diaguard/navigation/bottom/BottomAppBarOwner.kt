package com.faltenreich.diaguard.navigation.bottom

interface BottomAppBarOwner {

    val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible()
}