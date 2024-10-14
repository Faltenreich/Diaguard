package com.faltenreich.diaguard.food.nutrient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import com.faltenreich.diaguard.shared.view.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.food.form.FoodFormIntent
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun FoodNutrientList(
    data: List<FoodNutrientData>,
    onIntent: (FoodFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(AppTheme.colors.scheme.surfaceVariant)
                .padding(all = AppTheme.dimensions.padding.P_3),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = getString(Res.string.nutrient),
                modifier = Modifier.weight(1f),
                color = AppTheme.colors.scheme.onSurfaceVariant,
                style = AppTheme.typography.bodyMedium,
            )
            Text(
                text = getString(Res.string.per_100g),
                color = AppTheme.colors.scheme.onSurfaceVariant,
                style = AppTheme.typography.bodyMedium,
            )
        }
        data.forEach { data ->
            FoodNutrientListItem(
                data = data,
                onIntent = onIntent,
            )
            Divider()
        }
    }
}