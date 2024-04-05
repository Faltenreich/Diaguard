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
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormIntent
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewModel
import com.faltenreich.diaguard.measurement.type.form.MeasurementTypeFormViewState
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.TextCheckbox
import com.faltenreich.diaguard.shared.view.TextInput
import org.jetbrains.compose.resources.stringResource

@Composable
fun MeasurementValueRangeForm(
    viewState: MeasurementTypeFormViewState.Loaded,
    modifier: Modifier = Modifier,
    viewModel: MeasurementTypeFormViewModel = inject(),
) {
    Column(modifier = modifier) {
        TextCheckbox(
            title = stringResource(Res.string.value_range_highlighted),
            subtitle = stringResource(Res.string.value_range_highlighted_description),
            checked = viewModel.isValueRangeHighlighted.collectAsState().value,
            onCheckedChange = {
                viewModel.dispatchIntent(MeasurementTypeFormIntent.EditIsValueRangeHighlighted(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeMinimum.collectAsState().value,
            onInputChange = {
                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeMinimum(it))
            },
            label = getString(Res.string.value_range_minimum),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(viewState.unitName) },
            supportingText = { Text(stringResource(Res.string.value_range_minimum_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeLow.collectAsState().value,
            onInputChange = {
                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeLow(it))
            },
            label = getString(Res.string.value_range_low),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(viewState.unitName) },
            supportingText = { Text(stringResource(Res.string.value_range_low_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeTarget.collectAsState().value,
            onInputChange = {
                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeTarget(it))
            },
            label = getString(Res.string.value_range_target),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(viewState.unitName) },
            supportingText = { Text(stringResource(Res.string.value_range_target_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeHigh.collectAsState().value,
            onInputChange = {
                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeHigh(it))
            },
            label = getString(Res.string.value_range_high),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(viewState.unitName) },
            supportingText = { Text(stringResource(Res.string.value_range_high_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        )

        Divider()

        TextInput(
            input = viewModel.valueRangeMaximum.collectAsState().value,
            onInputChange = {
                viewModel.handleIntent(MeasurementTypeFormIntent.EditValueRangeMaximum(it))
            },
            label = getString(Res.string.value_range_maximum),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimensions.padding.P_3),
            suffix = { Text(viewState.unitName) },
            supportingText = { Text(stringResource(Res.string.value_range_maximum_description)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
        )
    }
}