package com.faltenreich.diaguard.navigation.screen

interface ScreenNavigation {

    suspend fun pushScreen(screen: Screen, popHistory: Boolean)

    suspend fun popScreen(): Boolean

    fun canPopScreen(): Boolean
}