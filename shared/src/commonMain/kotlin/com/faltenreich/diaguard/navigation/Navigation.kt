package com.faltenreich.diaguard.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.navigation.screen.Screen

class Navigation {

    lateinit var navigator: Navigator

    val lastItem: Screen?
        get() = navigator.lastItem as? Screen

    fun push(screen: Screen) {
        navigator.push(screen)
    }

    fun replaceAll(screen: Screen) {
        navigator.replaceAll(screen)
    }

    fun pop(): Boolean {
        return navigator.pop()
    }

    fun canPop(): Boolean {
        return navigator.canPop
    }
}