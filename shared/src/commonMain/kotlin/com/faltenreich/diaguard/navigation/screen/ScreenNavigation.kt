package com.faltenreich.diaguard.navigation.screen

interface ScreenNavigation {

    suspend fun pushScreen(screen: Screen, popHistory: Boolean)

    suspend fun popScreen(result: Pair<String, Any>? = null): Boolean

    suspend fun <T> collectLatestScreenResult(key: String, default: T? = null): T?

    fun canPopScreen(): Boolean
}