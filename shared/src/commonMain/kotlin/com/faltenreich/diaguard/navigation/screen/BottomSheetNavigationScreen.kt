package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigation

data object BottomSheetNavigationScreen : Screen {

    @Composable
    override fun Content() {
        BottomSheetNavigation()
    }
}