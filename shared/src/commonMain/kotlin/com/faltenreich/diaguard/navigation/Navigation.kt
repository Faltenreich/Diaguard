package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class Navigation(
    private val dispatcher: CoroutineDispatcher,
) {

    lateinit var navController: NavController
    lateinit var bottomSheetNavigator: BottomSheetNavigator
    var modal = MutableStateFlow<Modal?>(null)
    lateinit var snackbarState: SnackbarHostState

    val lastItem: Screen?
        // FIXME: Always fails
        get() = navController.currentBackStackEntry as? Screen

    suspend fun push(screen: Screen, popHistory: Boolean = false) = withContext(dispatcher) {
        // FIXME: Method setCurrentState must be called on the main thread
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
        bottomSheetNavigator.show(screen)
    }

    fun popBottomSheet() {
        bottomSheetNavigator.hide()
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