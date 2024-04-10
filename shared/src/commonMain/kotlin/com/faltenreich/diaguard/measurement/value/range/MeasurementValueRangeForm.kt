package com.faltenreich.diaguard.measurement.value.range

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.property.form.MeasurementPropertyFormViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
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
    unitName: String,
    modifier: Modifier = Modifier,
    viewModel: MeasurementPropertyFormViewModel = inject(),
) {
    Column(modifier = modifier) {
        TextCheckbox(
            title = getString(Res.string.value_range_highlighted),
            subtitle = getString(Res.string.value_range_highlighted_description),
            checked = viewModel.isValueRangeHighlighted.collectAsState().value,
            onCheckedChange = { viewModel.isValueRangeHighlighted.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeMinimum.collectAsState().value,
            onInputChange = { viewModel.valueRangeMinimum.value = it },
            label = getString(Res.string.value_range_minimum),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(unitName) },
            supportingText = { Text(getString(Res.string.value_range_minimum_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeLow.collectAsState().value,
            onInputChange = { viewModel.valueRangeLow.value = it },
            label = getString(Res.string.value_range_low),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(unitName) },
            supportingText = { Text(getString(Res.string.value_range_low_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeTarget.collectAsState().value,
            onInputChange = { viewModel.valueRangeTarget.value = it },
            label = getString(Res.string.value_range_target),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(unitName) },
            supportingText = { Text(getString(Res.string.value_range_target_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeHigh.collectAsState().value,
            onInputChange = { viewModel.valueRangeHigh.value = it },
            label = getString(Res.string.value_range_high),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(unitName) },
            supportingText = { Text(getString(Res.string.value_range_high_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeMaximum.collectAsState().value,
            onInputChange = { viewModel.valueRangeMaximum.value = it },
            label = getString(Res.string.value_range_maximum),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(unitName) },
            supportingText = { Text(getString(Res.string.value_range_maximum_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
        )
    }
}