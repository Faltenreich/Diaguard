package com.faltenreich.diaguard.statistic

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.localization.di.viewModel
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.statistic
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object StatisticScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.statistic))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<StatisticViewModel>()
        Statistic(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}