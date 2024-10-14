package com.faltenreich.diaguard.preference.decimal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable

@Serializable
data object DecimalPlacesFormScreen : Screen {

    @Composable
    override fun Content() {
        DecimalPlacesForm(viewModel = viewModel())
    }
}