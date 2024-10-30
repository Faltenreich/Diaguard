package com.faltenreich.diaguard.navigation.screen

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AndroidxScreenNavigation(
    private val dispatcher: CoroutineDispatcher,
) : ScreenNavigation {

    lateinit var navController: NavController

    override suspend fun pushScreen(screen: Screen, popHistory: Boolean) = withContext(dispatcher) {
        navController.navigate(
            route = screen,
            navOptions = NavOptions.Builder()
                .run {
                    if (popHistory) setPopUpTo(
                        route = navController.graph.findStartDestination().route,
                        inclusive = true,
                    )
                    else this
                }
                .build()
        )
    }

    override suspend fun popScreen(result: Pair<String, Any>?): Boolean = withContext(dispatcher) {
        result?.let { (key, value) ->
            val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle ?: return@let
            savedStateHandle[key] = value
        }
        navController.popBackStack()
    }

    override suspend fun <T> collectLatestScreenResult(key: String, default: T?): T? {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle ?: return null
        return savedStateHandle.getStateFlow(key, default).firstOrNull()
    }

    override fun canPopScreen(): Boolean {
        return navController.previousBackStackEntry != null
    }
}