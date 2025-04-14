package com.faltenreich.diaguard.timeline

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import kotlinx.serialization.Serializable

@Serializable
data object TimelineScreen : Screen {

    @Composable
    override fun Content(modifier: Modifier) {
        Timeline(viewModel = viewModel())
    }
}