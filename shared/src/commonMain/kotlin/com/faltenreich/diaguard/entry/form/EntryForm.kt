package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.rememberViewModel
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.datetime.format.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.DatePicker
import com.faltenreich.diaguard.shared.view.TimePicker
import dev.icerock.moko.resources.compose.stringResource

class EntryForm(private val entry: Entry?) : Screen {

    override val topAppBarStyle = TopAppBarStyle.CenterAligned {
        val viewModel = rememberViewModel { EntryFormViewModel(entry) }
        Text(stringResource(viewModel.title))
    }

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        floatingActionButton = {
            val viewModel = rememberViewModel { EntryFormViewModel(entry) }
            val navigator = LocalNavigator.currentOrThrow
            FloatingActionButton(
                onClick = {
                    viewModel.submit()
                    navigator.pop()
                },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
                Icon(Icons.Filled.Check, stringResource(MR.strings.entry_save))
            }
        }
    )

    @Composable
    override fun Content() {
        val viewModel = rememberViewModel { EntryFormViewModel(entry) }
        val formatter = inject<DateTimeFormatter>()
        val viewState = viewModel.viewState.collectAsState().value
        val datePickerState = remember { mutableStateOf(false) }
        val timePickerState = remember { mutableStateOf(false) }
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(onClick = { datePickerState.value = true }) {
                    Text(formatter.format(viewState.entry.dateTime.date))
                }
                Button(onClick = { timePickerState.value = true }) {
                    Text(formatter.format(viewState.entry.dateTime.time))
                }
            }
            TextField(
                value = viewState.entry.note ?: "",
                onValueChange = viewModel::setNote,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(MR.strings.note)) },
            )
        }
        if (datePickerState.value) {
            DatePicker(
                date = viewState.entry.dateTime.date,
                onPick = { date ->
                    datePickerState.value = false
                    viewModel.setDate(date)
                },
            )
        }
        if (timePickerState.value) {
            TimePicker(
                time = viewState.entry.dateTime.time,
                onPick = { date ->
                    timePickerState.value = false
                    viewModel.setTime(date)
                },
            )
        }
    }
}