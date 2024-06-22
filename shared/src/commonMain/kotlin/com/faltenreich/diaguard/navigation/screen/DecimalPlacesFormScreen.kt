package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

data object DecimalPlacesFormScreen : Screen {

    @Composable
    override fun Content() {
        Text("Decimal places")
    }
}