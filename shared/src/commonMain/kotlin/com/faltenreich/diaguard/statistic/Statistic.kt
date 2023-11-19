package com.faltenreich.diaguard.statistic

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Statistic(
    modifier: Modifier = Modifier,
    viewModel: StatisticViewModel = inject(),
) {
    Text("Statistic")
}