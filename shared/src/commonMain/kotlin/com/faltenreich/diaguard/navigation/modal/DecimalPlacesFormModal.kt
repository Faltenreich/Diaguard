package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesForm

data class DecimalPlacesFormModal(
    private val onDismissRequest: () -> Unit,
) : Modal {

    @Composable
    override fun Content() {
        DecimalPlacesForm(onDismissRequest)
    }
}