package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.carbohydrates_per_100g
import diaguard.shared.generated.resources.food

@Composable
fun FoodSearchHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = getString(Res.string.food),
            modifier = Modifier.weight(1f),
            color = AppTheme.colors.scheme.onPrimary,
            style = AppTheme.typography.bodyMedium,
        )
        Text(
            text = getString(Res.string.carbohydrates_per_100g),
            color = AppTheme.colors.scheme.onPrimary,
            style = AppTheme.typography.bodyMedium,
        )
    }
}