package com.faltenreich.diaguard.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.navigation.modal.Modal
import com.faltenreich.diaguard.navigation.screen.Screen
import kotlinx.coroutines.flow.MutableStateFlow

class Navigation {

    lateinit var navigator: Navigator
    var modal = MutableStateFlow<Modal?>(null)

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

    fun pushModal(modal: Modal) {
        this.modal.value = modal
    }

    fun popModal() {
        this.modal.value = null
    }
}