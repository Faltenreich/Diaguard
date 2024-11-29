package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HbA1cInfo(
    viewModel: HbA1cInfoViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state
    Text(modifier = modifier, text = "HbA1c Info")
}