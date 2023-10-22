package com.faltenreich.diaguard.export

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun ExportForm(
    modifier: Modifier = Modifier,
    viewModel: ExportFormViewModel = inject(),
) {
    Text("Export Form")
}