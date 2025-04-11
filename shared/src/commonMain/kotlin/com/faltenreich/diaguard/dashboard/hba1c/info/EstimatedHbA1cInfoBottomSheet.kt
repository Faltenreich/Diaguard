package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.screen.Screen

data object EstimatedHbA1cInfoBottomSheet : Screen {

    @Composable
    override fun Content(modifier: Modifier) {
        EstimatedHbA1cInfo()
    }
}