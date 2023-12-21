package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import cafe.adriel.voyager.core.screen.Screen as VoyagerScreen

/**
 * Component that can be navigated to
 *
 * State restoration requires every parameter to implement
 * [com.faltenreich.diaguard.shared.serialization.Serializable]
 */
sealed interface Screen : VoyagerScreen {

    val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.Hidden

    val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Hidden
}