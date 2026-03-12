package com.faltenreich.diaguard.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.app_name
import com.faltenreich.diaguard.resource.entry_new_description
import com.faltenreich.diaguard.resource.ic_add
import com.faltenreich.diaguard.resource.ic_search
import com.faltenreich.diaguard.resource.search_open
import com.faltenreich.diaguard.view.bar.BottomAppBarItem
import com.faltenreich.diaguard.view.bar.BottomAppBarStyle
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import com.faltenreich.diaguard.view.button.TooltipFloatingActionButton
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
                    painter = painterResource(Res.drawable.ic_add),
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