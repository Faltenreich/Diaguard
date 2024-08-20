package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.faltenreich.diaguard.navigation.bar.BarNavigation
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.bottomsheet.BottomSheetNavigation
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.navigation.modal.ModalNavigation
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.navigation.screen.ScreenNavigation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class Navigation(
    private val dispatcher: CoroutineDispatcher,
) : ScreenNavigation, BottomSheetNavigation, ModalNavigation, BarNavigation {

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

    override suspend fun pushScreen(screen: Screen, popHistory: Boolean) = withContext(dispatcher) {
        navController.navigate(
            route = screen,
            navOptions = NavOptions.Builder()
                .run {
                    if (popHistory) {
                        setPopUpTo(
                            route = navController.graph.findStartDestination().route,
                            inclusive = true,
                        )
                    } else {
                        this
                    }
                }
                .build()
        )
    }

    override suspend fun popScreen(): Boolean = withContext(dispatcher) {
        navController.popBackStack()
    }

    override fun canPopScreen(): Boolean {
        return navController.previousBackStackEntry != null
    }

    override fun openBottomSheet(bottomSheet: Screen) {
        _bottomSheet.tryEmit(bottomSheet)
    }

    override fun closeBottomSheet() {
        _bottomSheet.tryEmit(null)
    }

    override fun openModal(modal: Modal) {
        _modal.tryEmit(modal)
    }

    override fun closeModal() {
        _modal.tryEmit(null)
    }

    override suspend fun showSnackbar(
        message: String,
        actionLabel: String?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
    ) {
        snackbarState.showSnackbar(message, actionLabel, withDismissAction, duration)
    }
}