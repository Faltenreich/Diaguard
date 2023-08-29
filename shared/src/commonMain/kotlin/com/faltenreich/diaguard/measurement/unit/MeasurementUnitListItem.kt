package com.faltenreich.diaguard.measurement.unit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.view.FormRow
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementUnitListItem(
    unit: MeasurementUnit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { /* TODO */ }) {
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
        }
        Divider()
    }
}