package com.faltenreich.diaguard.statistic

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.statistic
import kotlinx.serialization.Serializable

@Serializable
data object StatisticScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.statistic))
        }

    @Composable
    override fun Content() {
        Statistic(viewModel = viewModel())
    }
}