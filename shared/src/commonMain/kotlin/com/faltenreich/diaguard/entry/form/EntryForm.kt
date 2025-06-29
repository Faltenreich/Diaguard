package com.faltenreich.diaguard.entry.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.picker.DatePickerDialog
import com.faltenreich.diaguard.datetime.picker.TimePickerDialog
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DeleteDialog
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.alarm
import diaguard.shared.generated.resources.ic_alarm
import diaguard.shared.generated.resources.ic_clear
import diaguard.shared.generated.resources.ic_note
import diaguard.shared.generated.resources.ic_tag
import diaguard.shared.generated.resources.ic_time
import diaguard.shared.generated.resources.minutes_until_notification
import diaguard.shared.generated.resources.note
import diaguard.shared.generated.resources.tag_remove_description

@Composable
fun EntryForm(
    viewModel: EntryFormViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return
    var note by remember { mutableStateOf(state.note) }
    var alarmDelayInMinutes by remember { mutableStateOf(state.alarmDelayInMinutes) }
    var tagQuery by remember { mutableStateOf(state.tags.query) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.animateContentSize(),
            shape = RectangleShape,
        ) {
            FormRow(icon = { ResourceIcon(Res.drawable.ic_time) }) {
                TextButton(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppTheme.colors.scheme.onSurfaceVariant,
                    ),
                ) {
                    Text(state.dateTime.dateLocalized)
                }
                TextButton(
                    onClick = { showTimePicker = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppTheme.colors.scheme.onSurfaceVariant,
                    ),
                ) {
                    Text(state.dateTime.timeLocalized)
                }
            }

            Divider()

            FormRow(icon = { ResourceIcon(Res.drawable.ic_tag) }) {
                EntryTagInput(
                    input = tagQuery,
                    onInputChange = { input ->
                        tagQuery = input
                        viewModel.dispatchIntent(EntryFormIntent.SetTagQuery(input))
                    },
                    suggestions = state.tags.suggestions,
                    onSuggestionSelected = { tag ->
                        viewModel.dispatchIntent(EntryFormIntent.AddTag(tag))
                        viewModel.dispatchIntent(EntryFormIntent.SetTagQuery(""))
                    }
                )
            }

            state.tags.selection.takeIf(List<*>::isNotEmpty)?.let { tags ->
                EntryTagList(
                    tags = tags,
                    onTagClick = { tag -> viewModel.dispatchIntent(EntryFormIntent.RemoveTag(tag)) },
                    trailingIcon = { tag ->
                        ResourceIcon(
                            icon = Res.drawable.ic_clear,
                            contentDescription = getString(Res.string.tag_remove_description, tag.name),
                            modifier = Modifier.size(InputChipDefaults.AvatarSize),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = AppTheme.dimensions.padding.P_3 +
                                AppTheme.dimensions.padding.P_3 +
                                AppTheme.dimensions.padding.P_3 +
                                AppTheme.dimensions.size.ImageMedium,
                            end = AppTheme.dimensions.padding.P_3,
                            bottom = AppTheme.dimensions.padding.P_2,
                        ),
                )
            }

            Divider()

            FormRow(icon = { ResourceIcon(Res.drawable.ic_note) }) {
                TextInput(
                    input = note,
                    onInputChange = { input ->
                        note = input
                        viewModel.dispatchIntent(EntryFormIntent.SetNote(input))
                    },
                    placeholder = { Text(getString(Res.string.note)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
                )
            }

            Divider()

            FormRow(icon = { ResourceIcon(Res.drawable.ic_alarm) }) {
                TextInput(
                    input = alarmDelayInMinutes?.toString() ?: "",
                    onInputChange = { input ->
                        val minutes = input.toIntOrNull()
                        alarmDelayInMinutes = minutes
                        viewModel.dispatchIntent(EntryFormIntent.SetAlarm(minutes))
                    },
                    placeholder = { Text(getString(Res.string.alarm)) },
                    suffix = {
                        if (state.alarmDelayInMinutes != null) {
                            Text(getString(Res.string.minutes_until_notification))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                )
            }
        }

        AnimatedVisibility(
            visible = state.measurements.isNotEmpty(),
            enter = fadeIn(),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.dimensions.padding.P_2_5),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2_5),
            ) {
                state.measurements.forEach { measurement ->
                    MeasurementCategoryInput(
                        state = measurement,
                        foodEaten = state.foodEaten,
                        onIntent = viewModel::dispatchIntent,
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            date = state.dateTime.date,
            onDismissRequest = { showDatePicker = false },
            onConfirmRequest = { date ->
                showDatePicker = false
                viewModel.dispatchIntent(EntryFormIntent.SetDate(date))
            },
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            time = state.dateTime.time,
            onDismissRequest = { showTimePicker = false },
            onConfirmRequest = { time ->
                showTimePicker = false
                viewModel.dispatchIntent(EntryFormIntent.SetTime(time))
            },
        )
    }

    if (state.deleteDialog != null) {
        DeleteDialog(
            onDismissRequest = {
                viewModel.dispatchIntent(EntryFormIntent.CloseDeleteDialog)
            },
            onConfirmRequest = {
                viewModel.dispatchIntent(EntryFormIntent.CloseDeleteDialog)
                viewModel.dispatchIntent(EntryFormIntent.Delete(needsConfirmation = false))
            },
        )
    }
}