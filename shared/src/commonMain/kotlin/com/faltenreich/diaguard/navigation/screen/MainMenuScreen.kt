package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.faltenreich.diaguard.navigation.menu.MainMenu

data class MainMenuScreen(private val navigator: Navigator) : Screen() {

    @Composable
    override fun Content() {
        MainMenu(navigator)
    }
}