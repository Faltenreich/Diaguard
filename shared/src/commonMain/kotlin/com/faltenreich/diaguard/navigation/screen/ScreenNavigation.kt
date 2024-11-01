package com.faltenreich.diaguard.navigation.screen

interface ScreenNavigation {

    suspend fun <T> collectLatestScreenResult(key: String, default: T? = null): T?

    fun canPopScreen(): Boolean
}