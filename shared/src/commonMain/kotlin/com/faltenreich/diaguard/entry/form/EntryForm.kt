package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.DatePicker
import com.faltenreich.diaguard.shared.view.TimePicker
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EntryForm(
    viewModel: EntryFormViewModel,
    modifier: Modifier = Modifier,
) {
    val dateTimeApi = inject<DateTimeApi>()
    val viewState = viewModel.viewState.collectAsState().value
    val datePickerState = remember { mutableStateOf(false) }
    val timePickerState = remember { mutableStateOf(false) }
    Column(modifier = modifier.padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(onClick = { datePickerState.value = true }) {
                Text(dateTimeApi.dateToLocalizedString(viewState.entry.dateTime.date))
            }
            Button(onClick = { timePickerState.value = true }) {
                Text(dateTimeApi.timeToLocalizedString(viewState.entry.dateTime.time))
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