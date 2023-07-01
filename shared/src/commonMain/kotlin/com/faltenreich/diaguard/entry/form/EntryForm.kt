package com.faltenreich.diaguard.entry.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.datetime.DateTimeFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.DatePicker
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.TimePicker
import com.faltenreich.diaguard.shared.view.ignoreParentPadding
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
        modifier = modifier.verticalScroll(rememberScrollState()).padding(AppTheme.dimensions.padding.P_3),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
    ) {
        FormRow(icon = { ResourceIcon(MR.images.ic_time) }) {
            TextButton(onClick = { datePickerState.isShown = true }) {
                Text(formatter.formatDate(viewModel.dateTime.date))
            }
            TextButton(onClick = { timePickerState.isShown = true }) {
                Text(formatter.formatTime(viewModel.dateTime.time))
            }
        }
        TextInput(
            input = viewModel.tag,
            onInputChange = { input -> viewModel.tag = input },
            label = stringResource(MR.strings.tag),
            leadingIcon = { ResourceIcon(MR.images.ic_tag) },
            modifier = Modifier.fillMaxWidth()
        )
        TextInput(
            input = viewModel.note,
            onInputChange = { input -> viewModel.note = input },
            label = stringResource(MR.strings.note),
            leadingIcon = { ResourceIcon(MR.images.ic_note) },
            modifier = Modifier.fillMaxWidth()
        )
        FormRow(icon = { ResourceIcon(MR.images.ic_alarm) }) {
            Text(stringResource(MR.strings.alarm_placeholder))
        }

        Divider(
            modifier = Modifier.ignoreParentPadding(horizontal = AppTheme.dimensions.padding.P_3),
        )

        viewModel.measurements.forEach { property ->
            property.typeInputDataList.forEachIndexed { index, type ->
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
                    leadingIcon = { if (index == 0) MeasurementPropertyIcon(property.property) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                )
            }
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