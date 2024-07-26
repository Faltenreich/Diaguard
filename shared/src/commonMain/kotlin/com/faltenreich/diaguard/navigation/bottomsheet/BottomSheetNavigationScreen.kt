package com.faltenreich.diaguard.navigation.bottomsheet

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen

data object BottomSheetNavigationScreen : Screen {

    @Composable
    override fun Content() {
        BottomSheetNavigation()
    }
}