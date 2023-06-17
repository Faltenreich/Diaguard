package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInput
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.DatePicker
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.TimePicker
import com.faltenreich.diaguard.shared.view.rememberDatePickerState
import com.faltenreich.diaguard.shared.view.rememberTimePickerState
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: EntryFormViewModel = inject(),
    formatter: DateTimeFormatter = inject(),
) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        FormRow(icon = { ResourceIcon(MR.images.ic_time) }) {
            TextButton(onClick = { datePickerState.isShown = true }) {
                Text(formatter.formatDate(viewModel.dateTime.date))
            }
            TextButton(onClick = { timePickerState.isShown = true }) {
                Text(formatter.formatTime(viewModel.dateTime.time))
            }
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_tag) }) {
            TextInput(
                input = "",
                hint = stringResource(MR.strings.tag),
                onInputChange = { TODO() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_note) }) {
            TextInput(
                input = viewModel.note,
                hint = stringResource(MR.strings.note),
                onInputChange = { input -> viewModel.note = input },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_alarm) }) {
            TextButton(onClick = {}) {
                TextButton(onClick = { TODO() }) {
                    Text(stringResource(MR.strings.alarm_placeholder))
                }
            }
        }
        Divider()
        viewModel.measurements.properties.forEach { property ->
            MeasurementPropertyInput(
                property = property,
                onInputChange = viewModel::updateMeasurementValue,
            )
        }
    }
    if (datePickerState.isShown) {
        DatePicker(
            date = viewModel.dateTime.date,
            onPick = { date ->
                datePickerState.isShown = false
                viewModel.date = date
            },
        )
    }
    if (timePickerState.isShown) {
        TimePicker(
            time = viewModel.dateTime.time,
            onPick = { time ->
                timePickerState.isShown = false
                viewModel.time = time
            },
        )
    }
}