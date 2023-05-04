package com.faltenreich.diaguard.timeline

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Timeline(
    viewModel: TimelineViewModel,
    modifier: Modifier = Modifier,
) {
    Text("Timeline", modifier = modifier)
}