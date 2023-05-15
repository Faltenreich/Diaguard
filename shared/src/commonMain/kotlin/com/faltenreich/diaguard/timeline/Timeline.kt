package com.faltenreich.diaguard.timeline

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Timeline(viewModel: TimelineViewModel = inject()) {
    Text("Timeline")
}