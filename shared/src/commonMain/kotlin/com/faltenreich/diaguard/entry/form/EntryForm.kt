package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Column
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
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.datetime.DateTimeApi
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.DatePicker
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.parameter.parametersOf

@Composable
fun EntryForm(
    entry: Entry?,
    modifier: Modifier = Modifier,
) {
    val viewModel = inject<EntryFormViewModel> { parametersOf(entry) }
    val dateTimeApi = inject<DateTimeApi>()
    val viewState = viewModel.viewState.collectAsState().value
    val datePickerState = remember { mutableStateOf(false) }
    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = viewState.entry.note ?: "",
            onValueChange = viewModel::setNote,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(MR.strings.note)) },
        )
        Button(onClick = { datePickerState.value = true }) {
            Text(dateTimeApi.dateTimeToLocalizedString(viewState.entry.dateTime))
        }
    }
    if (datePickerState.value) {
        DatePicker(
            dateTime = viewState.entry.dateTime,
            onDatePick = { dateTime ->
                datePickerState.value = false
                viewModel.setDateTime(dateTime)
            },
        )
    }
}