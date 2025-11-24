package com.faltenreich.diaguard.navigation.bar.top

sealed interface StatusBarStyle {

    data object Light : com.faltenreich.diaguard.navigation.bar.top.StatusBarStyle

    data object Dark : com.faltenreich.diaguard.navigation.bar.top.StatusBarStyle
}