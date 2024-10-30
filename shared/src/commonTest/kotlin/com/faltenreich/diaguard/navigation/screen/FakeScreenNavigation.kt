package com.faltenreich.diaguard.navigation.screen

class FakeScreenNavigation : ScreenNavigation {

    override suspend fun pushScreen(screen: Screen, popHistory: Boolean) = Unit

    override suspend fun popScreen(result: Pair<String, Any>?): Boolean = true

    override suspend fun <T> collectLatestScreenResult(key: String, default: T?): T? = null

    override fun canPopScreen(): Boolean = true
}