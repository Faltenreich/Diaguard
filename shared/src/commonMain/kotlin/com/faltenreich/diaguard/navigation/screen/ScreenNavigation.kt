package com.faltenreich.diaguard.navigation.screen

import kotlinx.coroutines.flow.StateFlow

interface ScreenNavigation {

    suspend fun pushScreen(screen: Screen, popHistory: Boolean)

    suspend fun popScreen(result: Pair<String, Any>? = null): Boolean

    fun <T> observeScreenResult(key: String, default: T? = null): StateFlow<T?>?

    fun canPopScreen(): Boolean
}