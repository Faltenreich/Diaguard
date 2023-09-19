package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DatePicker
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.TimePicker
import com.faltenreich.diaguard.shared.view.rememberDatePickerState
import com.faltenreich.diaguard.shared.view.rememberTimePickerState

@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: EntryFormViewModel = inject(),
) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FormRow(icon = { ResourceIcon(MR.images.ic_time) }) {
            TextButton(onClick = { datePickerState.isShown = true }) {
                Text(viewModel.dateFormatted)
            }
            TextButton(onClick = { timePickerState.isShown = true }) {
                Text(viewModel.timeFormatted)
            }
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_tag) }) {
            TextInput(
                input = viewModel.tag,
                onInputChange = { input -> viewModel.tag = input },
                label = getString(MR.strings.tag),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_note) }) {
            TextInput(
                input = viewModel.note,
                onInputChange = { input -> viewModel.note = input },
                label = getString(MR.strings.note),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Divider()
        FormRow(icon = { ResourceIcon(MR.images.ic_alarm) }) {
            Text(getString(MR.strings.alarm_placeholder))
        }
        Divider()
        viewModel.measurements.forEach { property ->
            FormRow(icon = { MeasurementPropertyIcon(property.property) }) {
                Column {
                    property.typeInputDataList.forEach { type ->
                        TextInput(
                            input = type.input,
                            onInputChange = { input ->
                                viewModel.updateMeasurementValue(
                                    type.copy(
                                        input = input
                                    )
                                )
                            },
                            label = type.type.name,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        )
                    }
                }
            }
            Divider()
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