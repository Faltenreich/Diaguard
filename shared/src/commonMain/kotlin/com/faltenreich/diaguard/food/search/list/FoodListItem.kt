package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodListItem(
    food: Food.Localized,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = AppTheme.dimensions.size.TouchSizeLarge)
            .padding(AppTheme.dimensions.padding.P_3),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = food.local.name,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = food.carbohydrates,
            style = AppTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodListItem(food = food().localized())
}