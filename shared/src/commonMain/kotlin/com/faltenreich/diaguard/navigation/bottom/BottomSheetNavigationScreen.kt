package com.faltenreich.diaguard.navigation.bottom

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.Screen

data object BottomSheetNavigationScreen : Screen {

    @Composable
    override fun Content() {
        BottomSheetContainer {
            BottomSheetNavigation()
        }
    }
}