package com.faltenreich.diaguard.navigation.screen

import androidx.navigation.NavController
import kotlinx.coroutines.flow.firstOrNull

class AndroidxScreenNavigation : ScreenNavigation {

    lateinit var navController: NavController

    override suspend fun <T> collectLatestScreenResult(key: String, default: T?): T? {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle ?: return null
        return savedStateHandle.getStateFlow(key, default).firstOrNull()
    }
}