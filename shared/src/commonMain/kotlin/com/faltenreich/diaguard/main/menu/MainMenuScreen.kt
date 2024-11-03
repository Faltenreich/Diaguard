package com.faltenreich.diaguard.main.menu

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf

@Serializable
data class MainMenuScreen(private val currentDestination: String?) : Screen {

    @Composable
    override fun Content() {
        // FIXME: Stays the same when opening screen with different parameters
        MainMenu(viewModel = viewModel(parameters = { parametersOf(currentDestination) }))
    }
}