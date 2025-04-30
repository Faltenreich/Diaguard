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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.faltenreich.diaguard.food.search.FoodSelectionEvent
import com.faltenreich.diaguard.food.search.FoodSelectionViewModel
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
    foodSelectionViewModel: FoodSelectionViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()
    val tags = viewModel.tagSelection.collectAsState().value

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        foodSelectionViewModel.collectEvents { event ->
            when (event) {
                is FoodSelectionEvent.Select ->
                    viewModel.dispatchIntent(EntryFormIntent.AddFood(event.food))
            }
        }
    }

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
                    Text(viewModel.dateFormatted)
                }
                TextButton(
                    onClick = { showTimePicker = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppTheme.colors.scheme.onSurfaceVariant,
                    ),
                ) {
                    Text(viewModel.timeFormatted)
                }
            }

            Divider()

            FormRow(icon = { ResourceIcon(Res.drawable.ic_tag) }) {
                EntryTagInput(
                    input = viewModel.tagQuery.collectAsState().value,
                    onInputChange = { viewModel.tagQuery.value = it },
                    suggestions = state?.tags ?: emptyList(),
                    onSuggestionSelected = { tag ->
                        viewModel.dispatchIntent(EntryFormIntent.AddTag(tag))
                        viewModel.tagQuery.value = ""
                    }
                )
            }

            if (tags.isNotEmpty()) {
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
                    input = viewModel.note,
                    onInputChange = { viewModel.note = it },
                    placeholder = { Text(getString(Res.string.note)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
                )
            }

            Divider()

            FormRow(icon = { ResourceIcon(Res.drawable.ic_alarm) }) {
                TextInput(
                    input = viewModel.alarmDelayInMinutes?.toString() ?: "",
                    onInputChange = { viewModel.alarmDelayInMinutes = it.toIntOrNull() },
                    placeholder = { Text(getString(Res.string.alarm)) },
                    suffix = {
                        if (viewModel.alarmDelayInMinutes != null) {
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

        val measurements = state?.measurements ?: emptyList()
        AnimatedVisibility(
            visible = measurements.isNotEmpty(),
            enter = fadeIn(),
        ) {
            Column(
                modifier = Modifier.padding(AppTheme.dimensions.padding.P_2_5),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2_5),
            ) {
                measurements.forEach { measurement ->
                    MeasurementCategoryInput(
                        state = measurement,
                        foodEaten = state?.foodEaten ?: emptyList(),
                        onIntent = viewModel::dispatchIntent,
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            date = viewModel.date,
            onDismissRequest = { showDatePicker = false },
            onConfirmRequest = { date ->
                showDatePicker = false
                viewModel.date = date
            },
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            time = viewModel.time,
            onDismissRequest = { showTimePicker = false },
            onConfirmRequest = { date ->
                showTimePicker = false
                viewModel.time = date
            },
        )
    }

    if (state?.deleteDialog != null) {
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