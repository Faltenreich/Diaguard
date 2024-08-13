package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.navigation.screen.Screen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class Navigation(private val dispatcher: CoroutineDispatcher) {

    lateinit var navController: NavController

    private val _currentScreen = MutableStateFlow<Screen?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    private val _topAppBarStyle = MutableStateFlow<TopAppBarStyle>(TopAppBarStyle.Hidden)
    val topAppBarStyle = _topAppBarStyle.asStateFlow()

    private val _bottomAppBarStyle = MutableStateFlow<BottomAppBarStyle>(BottomAppBarStyle.Hidden)
    val bottomAppBarStyle = _bottomAppBarStyle.asStateFlow()

    private val _bottomSheet = MutableStateFlow<Screen?>(null)
    val bottomSheet: StateFlow<Screen?> = _bottomSheet.asStateFlow()

    private val _modal = MutableStateFlow<Modal?>(null)
    val modal = _modal.asStateFlow()

    lateinit var snackbarState: SnackbarHostState

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.update { screen }
    }

    fun setTopAppBarStyle(topAppBarStyle: TopAppBarStyle) {
        _topAppBarStyle.update { topAppBarStyle }
    }

    fun setBottomAppBarStyle(bottomAppBarStyle: BottomAppBarStyle) {
        _bottomAppBarStyle.update { bottomAppBarStyle }
    }

    suspend fun push(screen: Screen, popHistory: Boolean = false) = withContext(dispatcher) {
        navController.navigate(
            route = screen,
            navOptions = NavOptions.Builder()
                // TODO: Support popHistory
                .build()
        )
    }

    suspend fun pop(): Boolean = withContext(dispatcher) {
        navController.popBackStack()
    }

    fun canPop(): Boolean {
        return navController.previousBackStackEntry != null
    }

    fun pushBottomSheet(screen: Screen) {
        _bottomSheet.tryEmit(screen)
    }

    fun popBottomSheet() {
        _bottomSheet.tryEmit(null)
    }

    fun pushModal(modal: Modal) {
        _modal.tryEmit(modal)
    }

    fun popModal() {
        _modal.tryEmit(null)
    }

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    ) {
        snackbarState.showSnackbar(message, actionLabel, withDismissAction, duration)
    }
}