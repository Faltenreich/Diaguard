package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.TimePicker
import com.faltenreich.diaguard.shared.view.rememberDatePickerState
import com.faltenreich.diaguard.shared.view.rememberTimePickerState
import dev.icerock.moko.resources.compose.stringResource

class EntryForm(private val entry: Entry? = null) : Screen {

    override val topAppBarStyle = TopAppBarStyle.CenterAligned {
        val viewModel = rememberViewModel { EntryFormViewModel(entry) }
        Text(stringResource(viewModel.title))
    }

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = {
            val viewModel = rememberViewModel { EntryFormViewModel(entry) }
            val navigator = LocalNavigator.currentOrThrow
            EntrySearchBottomAppBarItem(onClick = { viewModel.delete(); navigator.pop() })
        },
        floatingActionButton = {
            val viewModel = rememberViewModel { EntryFormViewModel(entry) }
            val navigator = LocalNavigator.currentOrThrow
            FloatingActionButton(onClick = { viewModel.submit(); navigator.pop() }) {
                Icon(Icons.Filled.Check, stringResource(MR.strings.entry_save))
            }
        }
    )

    @Composable
    override fun Content() {
        val viewModel = rememberViewModel { EntryFormViewModel(entry) }
        val formatter = inject<DateTimeFormatter>()
        val viewState = viewModel.viewState.collectAsState().value
        val datePickerState = rememberDatePickerState()
        val timePickerState = rememberTimePickerState()
        Column {
            FormRow(icon = MR.images.ic_time) {
                TextButton(onClick = { datePickerState.isShown = true }) {
                    Text(formatter.format(viewState.entry.createdAt.date))
                }
                TextButton(onClick = { timePickerState.isShown = true }) {
                    Text(formatter.format(viewState.entry.createdAt.time))
                }
            }
            Divider()
            FormRow(icon = MR.images.ic_tag) {
                TextInput(
                    input = "",
                    hint = stringResource(MR.strings.tag),
                    onInputChange = { TODO() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Divider()
            FormRow(icon = MR.images.ic_note) {
                TextInput(
                    input = viewState.entry.note ?: "",
                    hint = stringResource(MR.strings.note),
                    onInputChange = viewModel::setNote,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Divider()
            FormRow(icon = MR.images.ic_alarm) {
                TextButton(onClick = {}) {
                    TextButton(onClick = { TODO() }) {
                        Text(stringResource(MR.strings.alarm_placeholder))
                    }
                }
            }
        }
        if (datePickerState.isShown) {
            DatePicker(
                date = viewState.entry.createdAt.date,
                onPick = { date ->
                    datePickerState.isShown = false
                    viewModel.setDate(date)
                },
            )
        }
        if (timePickerState.isShown) {
            TimePicker(
                time = viewState.entry.createdAt.time,
                onPick = { date ->
                    timePickerState.isShown = false
                    viewModel.setTime(date)
                },
            )
        }
    }
}