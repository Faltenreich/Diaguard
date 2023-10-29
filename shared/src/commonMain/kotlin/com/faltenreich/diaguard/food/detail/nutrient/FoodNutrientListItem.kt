package com.faltenreich.diaguard.food.detail.nutrient

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.measurement.value.MeasurementValueFormatter
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow

@Composable
fun FoodNutrientListItem(
    data: FoodNutrientData,
    modifier: Modifier = Modifier,
    valueFormatter: MeasurementValueFormatter = inject(),
) {
    FormRow(modifier = modifier) {
        Text(
            text = getString(data.nutrient.label),
            modifier = Modifier.weight(1f),
        )
        Text(data.per100g?.let(valueFormatter::formatValue) ?: getString(MR.strings.placeholder))
    }
}