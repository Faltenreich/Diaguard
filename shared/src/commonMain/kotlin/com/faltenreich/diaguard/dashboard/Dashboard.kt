package com.faltenreich.diaguard.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Dashboard(viewModel: DashboardViewModel = inject()) {
    Text("Dashboard")
}