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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
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
    val state = viewModel.collectState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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
            Column {
                EntryTagInput(
                    input = viewModel.tagInput.collectAsState().value,
                    onInputChange = { viewModel.tagInput.value = it },
                    suggestions = state?.tags ?: emptyList(),
                    onSuggestionSelected = { tag ->
                        viewModel.dispatchIntent(EntryFormIntent.AddTag(tag))
                        viewModel.tagInput.value = ""
                    }
                )
                EntryTagList(
                    tags = viewModel.tags,
                    onTagClick = { tag -> viewModel.dispatchIntent(EntryFormIntent.RemoveTag(tag)) },
                )
            }
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
            TextInput(
                input = viewModel.alarmDelayInMinutes?.toString() ?: "",
                onInputChange = { viewModel.alarmDelayInMinutes = it.toIntOrNull() },
                label = getString(MR.strings.alarm),
                suffix = { Text(getString(MR.strings.minutes_until)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
            )
        }

        TextDivider(getString(MR.strings.measurement_properties))

        val properties = viewModel.measurements
        val foodEaten = viewModel.foodEaten
        properties.forEach { property ->
            MeasurementPropertyInput(
                data = property,
                foodEaten = foodEaten,
                onIntent = viewModel::dispatchIntent,
            )
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
}