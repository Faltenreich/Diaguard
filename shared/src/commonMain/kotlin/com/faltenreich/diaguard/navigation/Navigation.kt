package com.faltenreich.diaguard.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kotlinx.coroutines.flow.MutableStateFlow

class Navigation {

    lateinit var navigator: Navigator
    lateinit var bottomSheetNavigator: BottomSheetNavigator
    var modal = MutableStateFlow<Modal?>(null)
    lateinit var snackbarState: SnackbarHostState

    val lastItem: Screen?
        get() = navigator.lastItem as? Screen

    fun push(screen: Screen, popHistory: Boolean = false) {
        if (popHistory) {
            navigator.replaceAll(screen)
        } else {
            navigator.push(screen)
        }
    }

    fun pop(): Boolean {
        return navigator.pop()
    }

    fun canPop(): Boolean {
        return navigator.canPop
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