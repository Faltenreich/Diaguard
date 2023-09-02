package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.unit.MeasurementUnit
import com.faltenreich.diaguard.shared.view.FormRow
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementUnitListItem(
    unit: MeasurementUnit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { /* TODO */ }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(unit.name)
                    if (!unit.isDefault) {
                        Text(
                            text = stringResource(
                                MR.strings.measurement_unit_factor_description,
                                unit.factor.toString(), // TODO: Format
                                unit.type.units.first(MeasurementUnit::isDefault).name,
                            ),
                            style = AppTheme.typography.bodySmall,
                        )
                    }
                }
                if (unit.isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(MR.strings.measurement_unit_selected_description),
                        modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
                        tint = AppTheme.colors.Green,
                    )
                }
            }
        }
        Divider()
    }
}