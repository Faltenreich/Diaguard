package com.faltenreich.diaguard.navigation.screen

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class AndroidxScreenNavigation(
    private val dispatcher: CoroutineDispatcher,
) : ScreenNavigation {

    lateinit var navController: NavController

    private val _currentScreen = MutableStateFlow<Screen?>(null)
    override val currentScreen = _currentScreen.asStateFlow()

    private val _topAppBarStyle = MutableStateFlow<TopAppBarStyle>(TopAppBarStyle.Hidden)
    override val topAppBarStyle = _topAppBarStyle.asStateFlow()

    private val _bottomAppBarStyle = MutableStateFlow<BottomAppBarStyle>(BottomAppBarStyle.Visible())
    override val bottomAppBarStyle = _bottomAppBarStyle.asStateFlow()

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

    override fun setCurrentScreen(screen: Screen) {
        _currentScreen.update { screen }
    }

    override fun setTopAppBarStyle(topAppBarStyle: TopAppBarStyle) {
        _topAppBarStyle.update { topAppBarStyle }
    }

    override fun setBottomAppBarStyle(bottomAppBarStyle: BottomAppBarStyle) {
        _bottomAppBarStyle.update { bottomAppBarStyle }
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