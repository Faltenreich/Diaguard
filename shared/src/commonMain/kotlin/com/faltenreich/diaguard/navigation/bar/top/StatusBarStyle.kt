package com.faltenreich.diaguard.navigation.bar.top

sealed interface StatusBarStyle {

    data object Light : StatusBarStyle

    data object Dark : StatusBarStyle
}