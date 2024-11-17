package com.faltenreich.diaguard.preference.color

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable

@Serializable
data object ColorSchemeFormScreen : Screen {

    @Composable
    override fun Content() {
        ColorSchemeForm(viewModel = viewModel())
    }
}