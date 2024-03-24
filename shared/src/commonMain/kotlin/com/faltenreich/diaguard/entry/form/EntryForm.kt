package com.faltenreich.diaguard.entry.form

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.form.measurement.MeasurementPropertyInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagInput
import com.faltenreich.diaguard.entry.form.tag.EntryTagList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextInput

@Composable
fun EntryForm(
    modifier: Modifier = Modifier,
    viewModel: EntryFormViewModel = inject(),
) {
    val state = viewModel.collectState()

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Column(modifier = modifier.background(AppTheme.colors.scheme.surfaceVariant)) {
            FormRow(icon = { ResourceIcon(MR.images.ic_time) }) {
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

            FormRow(icon = { ResourceIcon(MR.images.ic_tag) }) {
                Column {
                    EntryTagInput(
                        input = viewModel.tag.collectAsState().value,
                        onInputChange = { viewModel.tag.value = it },
                        suggestions = state?.tags ?: emptyList(),
                        onSuggestionSelected = { tag ->
                            viewModel.dispatchIntent(EntryFormIntent.AddTag(tag))
                            viewModel.tag.value = ""
                        }
                    )
                    EntryTagList(
                        tags = viewModel.tags,
                        onTagClick = { tag -> viewModel.dispatchIntent(EntryFormIntent.RemoveTag(tag)) },
                        trailingIcon = { tag ->
                            ResourceIcon(
                                imageResource = MR.images.ic_clear,
                                contentDescription = getString(MR.strings.tag_remove_description, tag.name),
                                modifier = Modifier.size(InputChipDefaults.AvatarSize),
                            )
                        },
                        modifier = Modifier.padding(horizontal = AppTheme.dimensions.padding.P_3),
                    )
                }
            }

            Divider(modifier = Modifier.background(AppTheme.colors.scheme.onSurfaceVariant))

            FormRow(icon = { ResourceIcon(MR.images.ic_note) }) {
                TextInput(
                    input = viewModel.note,
                    onInputChange = { viewModel.note = it },
                    label = getString(MR.strings.note),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next ),
                )
            }

            Divider(modifier = Modifier.background(AppTheme.colors.scheme.onSurfaceVariant))

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
        }

        viewModel.measurements.forEach { measurement ->
            MeasurementPropertyInput(
                state = measurement,
                foodState = viewModel.foodEaten,
                onIntent = viewModel::dispatchIntent,
            )
            Divider()
        }
    }
}