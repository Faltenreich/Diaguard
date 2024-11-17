package com.faltenreich.diaguard.preference.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable

@Serializable
data object StartScreenFormScreen : Screen {

    @Composable
    override fun Content() {
        StartScreenForm(viewModel = viewModel())
    }
}