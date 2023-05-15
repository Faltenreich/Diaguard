package com.faltenreich.diaguard.timeline

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

class Timeline(private val date: Date = Date.today()) : Screen {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = {
            val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
            val viewState = viewModel.viewState.collectAsState().value
            EntrySearchBottomAppBarItem()
            DatePickerBottomAppBarItem(
                date = viewState.date,
                onDatePick = viewModel::setDate,
            )
        },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        val viewModel = getViewModel<TimelineViewModel> { parametersOf(date) }
        Text("Timeline")
    }
}