package com.faltenreich.diaguard.main.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.screen.Screen
import kotlinx.serialization.Serializable

@Serializable
data class MainMenuScreen(private val currentDestination: String?) : Screen {

    @Composable
    override fun Content(modifier: Modifier) {
        MainMenu(
            currentDestination = currentDestination,
            onDestinationChange = { _, _ -> TODO() },
        )
    }
}