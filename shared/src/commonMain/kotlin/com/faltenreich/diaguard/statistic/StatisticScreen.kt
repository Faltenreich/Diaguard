package com.faltenreich.diaguard.statistic

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.Screen
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString

data object StatisticScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.statistic))
        }

    @Composable
    override fun Content() {
        Statistic(viewModel = getViewModel())
    }
}