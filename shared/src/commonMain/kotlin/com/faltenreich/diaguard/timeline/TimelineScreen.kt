package com.faltenreich.diaguard.timeline

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

@Serializable
data object TimelineScreen : Screen {

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<TimelineViewModel>()
        return BottomAppBarStyle.Visible(
            actions = {
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = Res.string.search_open,
                    onClick = { viewModel.dispatchIntent(TimelineIntent.SearchEntries) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(TimelineIntent.CreateEntry) },
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
        Timeline(viewModel = viewModel())
    }
}