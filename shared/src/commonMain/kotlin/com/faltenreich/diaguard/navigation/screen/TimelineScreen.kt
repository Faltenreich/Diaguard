package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.timeline.Timeline
import com.faltenreich.diaguard.timeline.TimelineIntent
import com.faltenreich.diaguard.timeline.TimelineViewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_new_description
import diaguard.shared.generated.resources.ic_add
import diaguard.shared.generated.resources.ic_search
import diaguard.shared.generated.resources.search_open
import org.jetbrains.compose.resources.painterResource

data object TimelineScreen : Screen {

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TimelineViewModel>()
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = Res.string.search_open,
                    onClick = { viewModel.dispatchIntent(TimelineIntent.SearchEntries) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<TimelineViewModel>()
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

    @Composable
    override fun Content() {
        Timeline(viewModel = getViewModel())
    }
}