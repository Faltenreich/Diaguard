package com.faltenreich.diaguard.measurement.property.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.measurement.property.aggregationstyle.MeasurementAggregationStyleForm
import com.faltenreich.diaguard.measurement.property.range.MeasurementValueRangeForm
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.view.DeleteDialog
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.NoticeBar
import com.faltenreich.diaguard.shared.view.NoticeBarStyle
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.aggregation_style
import diaguard.shared.generated.resources.aggregation_style_description
import diaguard.shared.generated.resources.delete_error_pre_defined
import diaguard.shared.generated.resources.delete_title
import diaguard.shared.generated.resources.ic_check
import diaguard.shared.generated.resources.measurement_property_missing_input
import diaguard.shared.generated.resources.measurement_unit
import diaguard.shared.generated.resources.measurement_unit_select
import diaguard.shared.generated.resources.measurement_unit_selected_description
import diaguard.shared.generated.resources.name
import diaguard.shared.generated.resources.ok
import diaguard.shared.generated.resources.values
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementPropertyForm(
    state: MeasurementPropertyFormState?,
    onIntent: (MeasurementPropertyFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f),
        ) {
            var name by remember { mutableStateOf(state.property.name) }
            TextInput(
                input = name,
                onInputChange = { input ->
                    name = input
                    onIntent(MeasurementPropertyFormIntent.UpdateProperty(name = input))
                },
                label = stringResource(Res.string.name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = AppTheme.dimensions.padding.P_0,
                        top = AppTheme.dimensions.padding.P_3,
                        end = AppTheme.dimensions.padding.P_0,
                        bottom = AppTheme.dimensions.padding.P_0,
                    ),
            )

            TextDivider(stringResource(Res.string.measurement_unit))

            if (state.unitSuggestions.isNotEmpty()) {
                UnitList(state, onIntent)
            } else {
                UnitButton(
                    unit = state.property.unit,
                    onClick = { onIntent(MeasurementPropertyFormIntent.OpenUnitSearch) },
                )
            }

            TextDivider(stringResource(Res.string.values))

            FormRow(
                modifier = Modifier.clickable {
                    onIntent(
                        MeasurementPropertyFormIntent.OpenDialog(
                            MeasurementPropertyFormState.Dialog.AggregationStyle(state.property)
                        )
                    )
                },
            ) {
                Column(modifier = Modifier.weight(AppTheme.dimensions.weight.W_1)) {
                    Text(stringResource(Res.string.aggregation_style))
                    Text(
                        text = stringResource(Res.string.aggregation_style_description),
                        style = AppTheme.typography.bodySmall,
                    )
                }
                Text(stringResource(state.property.aggregationStyle.labelResource))
            }

            Divider()

            MeasurementValueRangeForm(
                state = state.valueRange,
                onUpdate = { onIntent(MeasurementPropertyFormIntent.UpdateValueRange(it)) },
            )
        }

        NoticeBar(
            text = stringResource(Res.string.measurement_property_missing_input),
            isVisible = state.errorBar != null,
            style = NoticeBarStyle.ERROR,
        )
    }

    state.dialog?.let { dialog ->
        Dialog(dialog, onIntent)
    }
}

@Composable
private fun UnitList(
    state: MeasurementPropertyFormState,
    onIntent: (MeasurementPropertyFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.unitSuggestions.forEachIndexed { index, item ->
            if (index != 0) {
                Divider()
            }
            FormRow(
                modifier = Modifier.clickable {
                    onIntent(MeasurementPropertyFormIntent.SelectUnit(item.unit))
                },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.title)
                        item.subtitle?.let { subtitle ->
                            Text(
                                text = subtitle,
                                style = AppTheme.typography.bodySmall,
                            )
                        }
                    }
                    AnimatedVisibility(visible = item.isSelected) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_check),
                            contentDescription = stringResource(
                                Res.string.measurement_unit_selected_description
                            ),
                            modifier = Modifier.size(AppTheme.dimensions.size.ImageMedium),
                            tint = AppTheme.colors.scheme.primary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UnitButton(
    unit: MeasurementUnit?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth(),
    ) {
        if (unit != null) {
            Column {
                Text(
                    text = unit.name,
                )
                Text(
                    text = unit.abbreviation,
                    style = AppTheme.typography.bodySmall,
                )
            }
        } else {
            Text(
                text = stringResource(Res.string.measurement_unit_select),
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun Dialog(
    state: MeasurementPropertyFormState.Dialog,
    onIntent: (MeasurementPropertyFormIntent) -> Unit,
) {
    when (state) {
        is MeasurementPropertyFormState.Dialog.Delete -> DeleteDialog(
            onDismissRequest = { onIntent(MeasurementPropertyFormIntent.CloseDialog) },
            onConfirmRequest = {
                onIntent(MeasurementPropertyFormIntent.CloseDialog)
                onIntent(MeasurementPropertyFormIntent.Delete(needsConfirmation = false))
            }
        )
        is MeasurementPropertyFormState.Dialog.Alert -> AlertDialog(
            onDismissRequest = { onIntent(MeasurementPropertyFormIntent.CloseDialog) },
            confirmButton = {
                TextButton(
                    onClick = { onIntent(MeasurementPropertyFormIntent.CloseDialog) },
                ) {
                    Text(
                        text = stringResource(Res.string.ok),
                        color = AppTheme.colors.scheme.onBackground,
                    )
                }
            },
            title = { Text(stringResource(Res.string.delete_title)) },
            text = { Text(stringResource(Res.string.delete_error_pre_defined)) },
        )
        is MeasurementPropertyFormState.Dialog.AggregationStyle -> ModalBottomSheet(
            onDismissRequest = { onIntent(MeasurementPropertyFormIntent.CloseDialog) },
        ) {
            MeasurementAggregationStyleForm(
                selection = state.selection.aggregationStyle,
                onChange = { aggregationStyle ->
                    onIntent(MeasurementPropertyFormIntent.UpdateAggregationStyle(aggregationStyle))
                },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    MeasurementPropertyForm(
        state = MeasurementPropertyFormState(
            property = property(),
            valueRange = MeasurementPropertyFormState.ValueRange(
                minimum = "minimum",
                low = "low",
                target = "target",
                high = "high",
                maximum = "maximum",
                isHighlighted = true,
                unit = null,
            ),
            unitSuggestions = emptyList(),
            errorBar = null,
            dialog = null,
        ),
        onIntent = {},
    )
}