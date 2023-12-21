package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.statistic.Statistic

data object StatisticScreen : Screen {

    @Composable
    override fun Content() {
        Statistic(viewModel = getViewModel())
    }
}