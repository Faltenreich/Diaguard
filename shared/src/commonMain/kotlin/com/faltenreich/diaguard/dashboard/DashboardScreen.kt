package com.faltenreich.diaguard.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable

@Serializable
data object DashboardScreen : Screen {

    @Composable
    override fun Content(modifier: Modifier) {
        Dashboard(viewModel = viewModel())
    }
}