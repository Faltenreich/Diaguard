package com.faltenreich.diaguard.navigation

import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import cafe.adriel.voyager.core.screen.Screen as VoyagerScreen

/**
 * Component that can be navigated to
 *
 * State restoration requires every parameter to implement
 * [com.faltenreich.diaguard.shared.serialization.Serializable]
 *
 * Both [topAppBarStyle] and [bottomAppBarStyle] must be computed
 * in order to avoid a BadParcelableException on resume
 */
interface Screen : VoyagerScreen {

    val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible()
}