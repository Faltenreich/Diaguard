package com.faltenreich.diaguard.dashboard

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.app_name
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

@Serializable
data object DashboardScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.app_name))
        }

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = viewModel<DashboardViewModel>()
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = Res.string.search_open,
                    onClick = { viewModel.dispatchIntent(DashboardIntent.SearchEntries) },
                )
            },
            floatingActionButton = {
                val viewModel = viewModel<DashboardViewModel>()
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(DashboardIntent.CreateEntry) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.entry_new_description),
                    )
                }
            },
        )

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.app_name))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<DashboardViewModel>()
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = Res.string.search_open,
                    onClick = { viewModel.dispatchIntent(DashboardIntent.SearchEntries) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(DashboardIntent.CreateEntry) },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = getString(Res.string.entry_new_description),
                    )
                }
            },
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<DashboardViewModel>()
        Dashboard(viewModel = viewModel)
    }
}