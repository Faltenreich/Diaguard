package com.faltenreich.diaguard.log

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DatePickerBottomAppBarItem
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

class Log(private val date: Date = Date.today()) : Screen {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = {
            val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
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
        val viewModel = getViewModel<LogViewModel> { parametersOf(date) }
        when (val viewState = viewModel.viewState.collectAsState().value) {
            is LogViewState.Requesting -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }
            is LogViewState.Responding -> EntryList(viewState.entries)
        }
    }
}