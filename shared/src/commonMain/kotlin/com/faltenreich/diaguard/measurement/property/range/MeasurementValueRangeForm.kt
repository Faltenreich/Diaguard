package com.faltenreich.diaguard.measurement.property.range

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormState
import com.faltenreich.diaguard.core.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.value_range_high_preference
import diaguard.shared.generated.resources.value_range_high_preference_description
import diaguard.shared.generated.resources.value_range_highlighted
import diaguard.shared.generated.resources.value_range_highlighted_description
import diaguard.shared.generated.resources.value_range_low_preference
import diaguard.shared.generated.resources.value_range_low_preference_description
import diaguard.shared.generated.resources.value_range_maximum
import diaguard.shared.generated.resources.value_range_maximum_description
import diaguard.shared.generated.resources.value_range_minimum
import diaguard.shared.generated.resources.value_range_minimum_description
import diaguard.shared.generated.resources.value_range_target_preference
import diaguard.shared.generated.resources.value_range_target_preference_description
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementValueRangeForm(
    state: MeasurementPropertyFormState.ValueRange,
    onUpdate: (MeasurementPropertyFormState.ValueRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    var minimum by remember { mutableStateOf(state.minimum) }
    var low by remember { mutableStateOf(state.low) }
    var target by remember { mutableStateOf(state.target) }
    var high by remember { mutableStateOf(state.high) }
    var maximum by remember { mutableStateOf(state.maximum) }
    var isHighlighted by remember { mutableStateOf(state.isHighlighted) }
    val unit = state.unit ?: ""

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .toggleable(
                    value = isHighlighted,
                    role = Role.Checkbox,
                    onValueChange = { isChecked ->
                        isHighlighted = isChecked
                        onUpdate(state.copy(isHighlighted = isChecked))
                    },
                )
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_3,
                    vertical = AppTheme.dimensions.padding.P_2,
                ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(getString(Res.string.value_range_highlighted))
                Text(
                    text = getString(Res.string.value_range_highlighted_description),
                    style = AppTheme.typography.bodySmall,
                )
            }
            Switch(
                checked = isHighlighted,
                onCheckedChange = null,
            )
        }

        Divider()

        TextInput(
            input = minimum,
            onInputChange = { input ->
                minimum = input
                onUpdate(state.copy(minimum = input))
            },
            label = getString(Res.string.value_range_minimum),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_2,
                ),
            placeholder = { Text(unit) },
            supportingText = { Text(getString(Res.string.value_range_minimum_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = low,
            onInputChange = { input ->
                low = input
                onUpdate(state.copy(low = input))
            },
            label = getString(Res.string.value_range_low_preference),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_2,
                ),
            placeholder = { Text(unit) },
            supportingText = { Text(getString(Res.string.value_range_low_preference_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = target,
            onInputChange = { input ->
                target = input
                onUpdate(state.copy(target = input))
            },
            label = getString(Res.string.value_range_target_preference),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_2,
                ),
            placeholder = { Text(unit) },
            supportingText = { Text(getString(Res.string.value_range_target_preference_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = high,
            onInputChange = { input ->
                high = input
                onUpdate(state.copy(high = input))
            },
            label = getString(Res.string.value_range_high_preference),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_2,
                ),
            placeholder = { Text(unit) },
            supportingText = { Text(getString(Res.string.value_range_high_preference_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = maximum,
            onInputChange = { input ->
                maximum = input
                onUpdate(state.copy(maximum = input))
            },
            label = getString(Res.string.value_range_maximum),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_2,
                ),
            placeholder = { Text(unit) },
            supportingText = { Text(getString(Res.string.value_range_maximum_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementValueRangeForm(
        state = MeasurementPropertyFormState.ValueRange(
            minimum = "minimum",
            low = "low",
            target = "target",
            high = "high",
            maximum = "maximum",
            isHighlighted = true,
            unit = null,
        ),
        onUpdate = {},
    )
}