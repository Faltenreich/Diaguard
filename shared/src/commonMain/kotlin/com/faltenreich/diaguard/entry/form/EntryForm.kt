package com.faltenreich.diaguard.entry.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.measurement.MeasurementCategoryInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.food.search.FoodSearchEvent
import com.faltenreich.diaguard.food.search.FoodSearchViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
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
import diaguard.shared.generated.resources.minutes_until
import diaguard.shared.generated.resources.note
import diaguard.shared.generated.resources.tag_remove_description

@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: EntryFormViewModel = inject(),
    foodSearchViewModel: FoodSearchViewModel = inject(),
) {
    val state = viewModel.collectState()

    LaunchedEffect(Unit) {
        foodSearchViewModel.collectEvents { event ->
            when (event) {
                is FoodSearchEvent.Select -> viewModel.dispatchIntent(EntryFormIntent.AddFood(event.food))
            }
        }
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Column(modifier = modifier.background(AppTheme.colors.scheme.surfaceVariant)) {
            FormRow(icon = { ResourceIcon(Res.drawable.ic_time) }) {
                TextButton(
                    onClick = { viewModel.dispatchIntent(EntryFormIntent.SelectDate) },
                    colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.scheme.onSurfaceVariant),
                ) {
                    Text(viewModel.dateFormatted)
                }
                TextButton(
                    onClick = { viewModel.dispatchIntent(EntryFormIntent.SelectDate) },
                    colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.scheme.onSurfaceVariant),
                ) {
                    Text(viewModel.timeFormatted)
                }
            }

            Divider(modifier = Modifier.background(AppTheme.colors.scheme.onSurfaceVariant))

            FormRow(icon = { ResourceIcon(Res.drawable.ic_tag) }) {
                Column {
                    EntryTagInput(
                        input = viewModel.tagQuery.collectAsState().value,
                        onInputChange = { viewModel.tagQuery.value = it },
                        suggestions = state?.tags ?: emptyList(),
                        onSuggestionSelected = { tag ->
                            viewModel.dispatchIntent(EntryFormIntent.AddTag(tag))
                            viewModel.tagQuery.value = ""
                        }
                    )
                    EntryTagList(
                        tags = viewModel.tagSelection.collectAsState().value,
                        onTagClick = { tag -> viewModel.dispatchIntent(EntryFormIntent.RemoveTag(tag)) },
                        trailingIcon = { tag ->
                            ResourceIcon(
                                icon = Res.drawable.ic_clear,
                                contentDescription = getString(Res.string.tag_remove_description, tag.name),
                                modifier = Modifier.size(InputChipDefaults.AvatarSize),
                            )
                        },
                        modifier = Modifier.padding(horizontal = AppTheme.dimensions.padding.P_3),
                    )
                }
            }

            Divider(modifier = Modifier.background(AppTheme.colors.scheme.onSurfaceVariant))

            FormRow(icon = { ResourceIcon(Res.drawable.ic_note) }) {
                TextInput(
                    input = viewModel.note,
                    onInputChange = { viewModel.note = it },
                    label = getString(Res.string.note),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
                )
            }

            Divider(modifier = Modifier.background(AppTheme.colors.scheme.onSurfaceVariant))

            FormRow(icon = { ResourceIcon(Res.drawable.ic_alarm) }) {
                TextInput(
                    input = viewModel.alarmDelayInMinutes?.toString() ?: "",
                    onInputChange = { viewModel.alarmDelayInMinutes = it.toIntOrNull() },
                    label = getString(Res.string.alarm),
                    suffix = { Text(getString(Res.string.minutes_until)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                )
            }
        }

        AnimatedVisibility(
            visible = viewModel.measurements.isNotEmpty(),
            enter = fadeIn(),
        ) {
            Column {
                viewModel.measurements.forEach { measurement ->
                    MeasurementCategoryInput(
                        state = measurement,
                        foodState = viewModel.foodEaten,
                        onIntent = viewModel::dispatchIntent,
                    )
                    Divider()
                }
            }
        }
    }
}