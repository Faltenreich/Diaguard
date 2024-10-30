package com.faltenreich.diaguard.navigation.screen

import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import kotlinx.coroutines.flow.StateFlow

interface ScreenNavigation {

    val currentScreen: StateFlow<Screen?>

    val topAppBarStyle: StateFlow<TopAppBarStyle>

    val bottomAppBarStyle: StateFlow<BottomAppBarStyle>

    suspend fun pushScreen(screen: Screen, popHistory: Boolean)

    fun setCurrentScreen(screen: Screen)

    fun setTopAppBarStyle(topAppBarStyle: TopAppBarStyle)

    fun setBottomAppBarStyle(bottomAppBarStyle: BottomAppBarStyle)

    suspend fun popScreen(result: Pair<String, Any>? = null): Boolean

    suspend fun <T> collectLatestScreenResult(key: String, default: T? = null): T?

    fun canPopScreen(): Boolean
}