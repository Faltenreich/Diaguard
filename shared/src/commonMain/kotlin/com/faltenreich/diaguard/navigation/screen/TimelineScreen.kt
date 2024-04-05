package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.timeline.Timeline
import com.faltenreich.diaguard.timeline.TimelineIntent
import com.faltenreich.diaguard.timeline.TimelineViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class TimelineScreen(val date: Date? = null) : Screen {

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = Res.string.search_open,
                    onClick = { viewModel.dispatchIntent(TimelineIntent.SearchEntries) },
                )
                BottomAppBarItem(
                    painter = painterResource(Res.drawable.ic_date_range),
                    contentDescription = Res.string.date_pick,
                    onClick = { viewModel.dispatchIntent(TimelineIntent.SelectDate) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
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
        Timeline(viewModel = getViewModel { parametersOf(date) })
    }
}