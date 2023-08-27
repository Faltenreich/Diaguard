package com.faltenreich.diaguard.measurement.type.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.unit.MeasurementTypeUnit
import com.faltenreich.diaguard.shared.view.FormRow
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MeasurementTypeUnitListItem(
    typeUnit: MeasurementTypeUnit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FormRow(icon = { /* TODO */ }) {
            Column(modifier = Modifier.weight(1f)) {
                Text(typeUnit.unit.name)
                Text(
                    text = typeUnit.factor.toString(), // TODO: Format
                    style = AppTheme.typography.bodySmall,
                )
            }
            if (typeUnit.type.selectedTypeUnitId == typeUnit.unitId) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(MR.strings.measurement_type_unit_selected_description),
                    modifier = modifier.size(AppTheme.dimensions.size.ImageMedium),
                    tint = AppTheme.colors.Green,
                )
            }
        }
        Divider()
    }
}