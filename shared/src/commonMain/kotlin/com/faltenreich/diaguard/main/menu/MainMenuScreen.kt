package com.faltenreich.diaguard.main.menu

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen

data object MainMenuScreen : Screen {

    @Composable
    override fun Content() {
        MainMenu()
    }
}