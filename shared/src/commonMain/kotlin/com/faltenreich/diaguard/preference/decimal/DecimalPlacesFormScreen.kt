package com.faltenreich.diaguard.preference.decimal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bottom.BottomSheetContainer
import com.faltenreich.diaguard.navigation.Screen

data object DecimalPlacesFormScreen : Screen {

    @Composable
    override fun Content() {
        BottomSheetContainer {
            DecimalPlacesForm()
        }
    }
}