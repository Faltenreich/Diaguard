package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel

data object HbA1cInfoBottomSheet : Screen {

    @Composable
    override fun Content() {
        HbA1cInfo(viewModel = viewModel())
    }
}