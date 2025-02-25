package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen

data object EstimatedHbA1cInfoBottomSheet : Screen {

    @Composable
    override fun Content() {
        EstimatedHbA1cInfo()
    }
}