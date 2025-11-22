package com.faltenreich.diaguard.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
import diaguard.core.view.generated.resources.ic_add
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.app_name
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data object DashboardScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.app_name))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<DashboardViewModel>()
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = stringResource(Res.string.search_open),
                    onClick = { viewModel.dispatchIntent(DashboardIntent.SearchEntries) },
                )
            },
            floatingActionButton = {
                TooltipFloatingActionButton(
                    painter = painterResource(diaguard.core.view.generated.resources.Res.drawable.ic_add),
                    contentDescription = stringResource(Res.string.entry_new_description),
                    onClick = { viewModel.dispatchIntent(DashboardIntent.CreateEntry) },
                )
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<DashboardViewModel>()
        Dashboard(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}