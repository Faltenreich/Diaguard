package com.faltenreich.diaguard.measurement.value.range

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextCheckbox
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.value_range_high
import diaguard.shared.generated.resources.value_range_high_description
import diaguard.shared.generated.resources.value_range_highlighted
import diaguard.shared.generated.resources.value_range_highlighted_description
import diaguard.shared.generated.resources.value_range_low
import diaguard.shared.generated.resources.value_range_low_description
import diaguard.shared.generated.resources.value_range_maximum
import diaguard.shared.generated.resources.value_range_maximum_description
import diaguard.shared.generated.resources.value_range_minimum
import diaguard.shared.generated.resources.value_range_minimum_description
import diaguard.shared.generated.resources.value_range_target
import diaguard.shared.generated.resources.value_range_target_description

@Composable
fun MeasurementValueRangeForm(
    state: MeasurementValueRangeState,
    onUpdate: (MeasurementValueRangeState) -> Unit,
    modifier: Modifier = Modifier,
) {
    var minimum by remember { mutableStateOf(state.minimum) }
    var low by remember { mutableStateOf(state.low) }
    var target by remember { mutableStateOf(state.target) }
    var high by remember { mutableStateOf(state.high) }
    var maximum by remember { mutableStateOf(state.maximum) }
    var isHighlighted by remember { mutableStateOf(state.isHighlighted) }

    Column(modifier = modifier) {
        FormRow {
            TextCheckbox(
                title = getString(Res.string.value_range_highlighted),
                subtitle = getString(Res.string.value_range_highlighted_description),
                checked = isHighlighted,
                onCheckedChange = { isChecked ->
                    isHighlighted = isChecked
                    onUpdate(state.copy(isHighlighted = isChecked))
                },
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
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
            suffix = { Text(state.unit) },
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
            label = getString(Res.string.value_range_low),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
            suffix = { Text(state.unit) },
            supportingText = { Text(getString(Res.string.value_range_low_description)) },
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
            label = getString(Res.string.value_range_target),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
            suffix = { Text(state.unit) },
            supportingText = { Text(getString(Res.string.value_range_target_description)) },
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
            label = getString(Res.string.value_range_high),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_1,
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
            suffix = { Text(state.unit) },
            supportingText = { Text(getString(Res.string.value_range_high_description)) },
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
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
            suffix = { Text(state.unit) },
            supportingText = { Text(getString(Res.string.value_range_maximum_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
        )
    }
}