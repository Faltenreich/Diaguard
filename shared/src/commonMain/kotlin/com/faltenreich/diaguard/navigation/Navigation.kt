package com.faltenreich.diaguard.navigation

import androidx.navigation.NavController
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.snack.SnackbarNavigation
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.modal.ModalNavigation
import com.faltenreich.diaguard.navigation.screen.Screen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class Navigation(
    private val dispatcher: CoroutineDispatcher,
    private val bottomSheetNavigation: BottomSheetNavigation,
    private val modalNavigation: ModalNavigation,
    private val snackbarNavigation: SnackbarNavigation,
) : BottomSheetNavigation by bottomSheetNavigation,
    ModalNavigation by modalNavigation,
    SnackbarNavigation by snackbarNavigation
{

    private val events = MutableSharedFlow<NavigationEvent>()

    suspend fun postEvent(event: NavigationEvent) = withContext(dispatcher) {
        events.emit(event)
    }

    suspend fun collectEvents(onEvent: (NavigationEvent) -> Unit) {
        events.collect(onEvent)
    }

    private val _currentScreen = MutableStateFlow<Screen?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    private val _topAppBarStyle = MutableStateFlow<TopAppBarStyle>(TopAppBarStyle.Hidden)
    val topAppBarStyle = _topAppBarStyle.asStateFlow()

    private val _bottomAppBarStyle = MutableStateFlow<BottomAppBarStyle>(BottomAppBarStyle.Visible())
    val bottomAppBarStyle = _bottomAppBarStyle.asStateFlow()

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.update { screen }
    }

    fun setTopAppBarStyle(topAppBarStyle: TopAppBarStyle) {
        _topAppBarStyle.update { topAppBarStyle }
    }

    fun setBottomAppBarStyle(bottomAppBarStyle: BottomAppBarStyle) {
        _bottomAppBarStyle.update { bottomAppBarStyle }
    }

    // TODO: Remove navController
    lateinit var navController: NavController

    suspend fun <T> collectLatestScreenResult(key: String, default: T?): T? {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle ?: return null
        return savedStateHandle.getStateFlow(key, default).firstOrNull()
    }
}