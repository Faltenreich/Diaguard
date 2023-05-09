package com.faltenreich.diaguard.timeline

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.rememberViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DatePickerBottomAppBarItem

class Timeline(date: Date = Date.today()) : Screen {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = {
            val viewModel = rememberViewModel { TimelineViewModel(date) }
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
        Text("Timeline")
    }
}