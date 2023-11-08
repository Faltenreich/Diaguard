package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.dashboard.Dashboard
import com.faltenreich.diaguard.shared.di.getViewModel

data object DashboardScreen : Screen() {

    @Composable
    override fun Content() {
        Dashboard(viewModel = getViewModel())
    }
}