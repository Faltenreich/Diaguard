package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.alarm.AlarmPicker
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DatePicker
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.TimePicker

@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: EntryFormViewModel = inject(),
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showAlarmPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        FormRow(icon = { ResourceIcon(MR.images.ic_time) }) {
            TextButton(onClick = { showDatePicker = true }) {
                Text(viewModel.dateFormatted)
            }
            TextButton(onClick = { showTimePicker = true }) {
                Text(viewModel.timeFormatted)
            }
        }

        Divider()

        FormRow(icon = { ResourceIcon(MR.images.ic_tag) }) {
            TextInput(
                input = viewModel.tag,
                onInputChange = { viewModel.tag = it },
                label = getString(MR.strings.tag),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
            )
        }

        Divider()

        FormRow(icon = { ResourceIcon(MR.images.ic_note) }) {
            TextInput(
                input = viewModel.note,
                onInputChange = { viewModel.note = it },
                label = getString(MR.strings.note),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
            )
        }

        Divider()

        FormRow(icon = { ResourceIcon(MR.images.ic_alarm) }) {
            TextButton(onClick = { showAlarmPicker = true }) {
                Text(viewModel.alarmDelayFormatted)
            }
        }

        TextDivider(getString(MR.strings.measurement_properties))

        val properties = viewModel.measurements
        properties.forEachIndexed { propertyIndex, property ->
            FormRow(icon = { MeasurementPropertyIcon(property.property) }) {
                Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1)) {
                    val types = property.typeInputDataList
                    types.forEachIndexed { typeIndex, type ->
                        val isLast = propertyIndex == properties.size - 1 && typeIndex == types.size - 1
                        TextInput(
                            input = type.input,
                            onInputChange = { input ->
                                viewModel.updateMeasurementValue(type.copy(input = input))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = type.type.name,
                            suffix = { Text(type.type.selectedUnit?.abbreviation ?: "") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
                            ),
                        )
                    }
                }
            }
            Divider()
        }
    }

    if (showDatePicker) {
        DatePicker(
            date = viewModel.dateTime.date,
            onPick = { date ->
                showDatePicker = false
                viewModel.date = date
            },
        )
    }

    if (showTimePicker) {
        TimePicker(
            time = viewModel.dateTime.time,
            onPick = { time ->
                showTimePicker = false
                viewModel.time = time
            },
        )
    }

    AlarmPicker(
        expanded = showAlarmPicker,
        onDismissRequest = { showAlarmPicker = false },
        delay = viewModel.alarmDelay,
        onPick = { viewModel.alarmDelay = it },
    )
}