package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.export.ExportForm
import com.faltenreich.diaguard.shared.di.getViewModel

data object ExportFormScreen : Screen() {

    @Composable
    override fun Content() {
        ExportForm(viewModel = getViewModel())
    }
}