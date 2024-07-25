package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class Navigation(
    private val dispatcher: CoroutineDispatcher,
) {

    lateinit var navController: NavController
    var modal = MutableStateFlow<Modal?>(null)
    lateinit var snackbarState: SnackbarHostState

    private val _currentScreen = MutableStateFlow<Screen?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.update { screen }
    }

    suspend fun push(screen: Screen, popHistory: Boolean = false) = withContext(dispatcher) {
        navController.navigate(
            route = screen,
            navOptions = NavOptions.Builder()
                // TODO: Support popHistory
                .build()
        )
    }

    fun pop(): Boolean {
        return navController.popBackStack()
    }

    fun canPop(): Boolean {
        return navController.previousBackStackEntry != null
    }

    fun pushBottomSheet(screen: Screen) {
        TODO()
    }

    fun popBottomSheet() {
        TODO()
    }

    fun pushModal(modal: Modal) {
        this.modal.value = modal
    }

    fun popModal() {
        this.modal.value = null
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