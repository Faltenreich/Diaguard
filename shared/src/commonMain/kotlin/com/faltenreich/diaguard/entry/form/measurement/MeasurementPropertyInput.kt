package com.faltenreich.diaguard.entry.form.measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.form.EntryFormIntent
import com.faltenreich.diaguard.food.input.FoodInput
import com.faltenreich.diaguard.measurement.property.MeasurementPropertyIcon
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun MeasurementPropertyInput(
    data: MeasurementPropertyInputData,
    onIntent: (EntryFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormRow(
        icon = { MeasurementPropertyIcon(data.property) },
        modifier = modifier,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1)) {
            val types = data.typeInputDataList
            types.forEach { type ->
                MeasurementTypeInput(
                    data = type,
                    onIntent = onIntent,
                )
            }
            if (data.property.isMeal) {
                FoodInput(
                    onAdd = { onIntent(EntryFormIntent.AddFood) },
                )
            }
        }
    }
}