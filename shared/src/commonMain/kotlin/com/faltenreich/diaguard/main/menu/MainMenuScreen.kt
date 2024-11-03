package com.faltenreich.diaguard.main.menu

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable

@Serializable
data class MainMenuScreen(private val currentDestination: String?) : Screen {

    @Composable
    override fun Content() {
        MainMenu(
            currentDestination = currentDestination,
            viewModel = viewModel(),
        )
    }
}