package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.timeline.Timeline
import com.faltenreich.diaguard.timeline.TimelineIntent
import com.faltenreich.diaguard.timeline.TimelineViewModel
import dev.icerock.moko.resources.compose.painterResource
import org.koin.core.parameter.parametersOf

data class TimelineScreen(val date: Date? = null) : Screen {

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
                val viewState = viewModel.collectState()
                BottomAppBarItem(
                    painter = painterResource(MR.images.ic_search),
                    contentDescription = MR.strings.search_open,
                    onClick = { viewModel.dispatchIntent(TimelineIntent.SearchEntries) },
                )
                DatePickerBottomAppBarItem(
                    date = viewState?.currentDate,
                    onDatePick = { viewModel.dispatchIntent(TimelineIntent.SetDate(it)) },
                )
            },
            floatingActionButton = {
                val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
                FloatingActionButton(
                    onClick = { viewModel.dispatchIntent(TimelineIntent.CreateEntry) },
                ) {
                    Icon(
                        painter = painterResource(MR.images.ic_add),
                        contentDescription = getString(MR.strings.entry_new_description),
                    )
                }
            },
        )

    @Composable
    override fun Content() {
        Timeline(viewModel = getViewModel { parametersOf(date) })
    }
}