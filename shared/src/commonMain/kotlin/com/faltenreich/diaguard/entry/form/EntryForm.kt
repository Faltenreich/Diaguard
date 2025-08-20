package com.faltenreich.diaguard.entry.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.datetime.picker.DatePickerDialog
import com.faltenreich.diaguard.datetime.picker.TimePickerDialog
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInput
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInputState
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInputState
import com.faltenreich.diaguard.entry.form.tag.EntryTagInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DeleteDialog
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.ic_alarm
import diaguard.shared.generated.resources.ic_clear
import diaguard.shared.generated.resources.ic_note
import diaguard.shared.generated.resources.ic_tag
import diaguard.shared.generated.resources.ic_time
import diaguard.shared.generated.resources.note
import diaguard.shared.generated.resources.reminder_picker_open
import diaguard.shared.generated.resources.tag_remove_description
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntryForm(
    state: EntryFormState?,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    var note by remember { mutableStateOf(state.note) }
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
                        onIntent(EntryFormIntent.SetTagQuery(input))
                    },
                    suggestions = state.tags.suggestions,
                    onSuggestionSelect = { tag ->
                        tagQuery = ""
                        onIntent(EntryFormIntent.SetTagQuery(""))
                        onIntent(EntryFormIntent.AddTag(tag))
                    }
                )
            }

            state.tags.selection.takeIf(Collection<*>::isNotEmpty)?.let { tags ->
                EntryTagList(
                    tags = tags,
                    onTagClick = { tag -> onIntent(EntryFormIntent.RemoveTag(tag)) },
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
                    trailingIcon = { tag ->
                        ResourceIcon(
                            icon = Res.drawable.ic_clear,
                            contentDescription = getString(Res.string.tag_remove_description, tag.name),
                            modifier = Modifier.size(InputChipDefaults.AvatarSize),
                        )
                    },
                )
            }

            Divider()

            FormRow(icon = { ResourceIcon(Res.drawable.ic_note) }) {
                TextInput(
                    input = note,
                    onInputChange = { input ->
                        note = input
                        onIntent(EntryFormIntent.SetNote(input))
                    },
                    placeholder = { Text(getString(Res.string.note)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
                )
            }

            Divider()

            FormRow(
                icon = { ResourceIcon(Res.drawable.ic_alarm) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClickLabel = stringResource(Res.string.reminder_picker_open),
                        role = Role.Button,
                        onClick = { onIntent(EntryFormIntent.OpenReminderPicker) },
                    ),
            ) {
                Text(
                    text = state.reminder.label,
                    modifier = Modifier.weight(1f),
                )
                if (state.reminder.showMissingPermissionInfo) {
                    ResourceIcon(
                        icon = Res.drawable.ic_note,
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
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
                        onIntent = onIntent,
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
                onIntent(EntryFormIntent.SetDate(date))
            },
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            time = state.dateTime.time,
            onDismissRequest = { showTimePicker = false },
            onConfirmRequest = { time ->
                showTimePicker = false
                onIntent(EntryFormIntent.SetTime(time))
            },
        )
    }

    if (state.deleteDialog != null) {
        DeleteDialog(
            onDismissRequest = {
                onIntent(EntryFormIntent.CloseDeleteDialog)
            },
            onConfirmRequest = {
                onIntent(EntryFormIntent.CloseDeleteDialog)
                onIntent(EntryFormIntent.Delete(needsConfirmation = false))
            },
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val dateTime = now()
    EntryForm(
        state = EntryFormState(
            dateTime = EntryFormState.DateTime(
                date = dateTime.date,
                dateLocalized = dateTime.date.toString(),
                time = dateTime.time,
                timeLocalized = dateTime.time.toString(),
            ),
            note = "Note",
            reminder = EntryFormState.Reminder(
                delayInMinutes = 10,
                label = "In 10 Minutes",
                showMissingPermissionInfo = false,
            ),
            measurements = listOf(
                MeasurementCategoryInputState(
                    category = category(),
                    propertyInputStates = listOf(
                        MeasurementPropertyInputState(
                            property = property(),
                            input = "Value",
                            isLast = true,
                            error = null,
                            decimalPlaces = 3,
                        ),
                    ),
                ),
            ),
            foodEaten = emptyList(),
            tags = EntryFormState.Tags(
                query = "",
                suggestions = emptyList(),
                selection = listOf(tag()),
            ),
            deleteDialog = null,
        ),
        onIntent = {},
    )
}