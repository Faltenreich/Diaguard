package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.food.search.FoodSearchIntent
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_create
import diaguard.shared.generated.resources.food_search_empty_description
import diaguard.shared.generated.resources.food_search_empty_title

@Composable
fun FoodListEmpty(
    modifier: Modifier = Modifier,
    onIntent: (FoodSearchIntent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = AppTheme.dimensions.padding.P_3,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.food_search_empty_title),
            fontWeight = FontWeight.Bold,
            style = AppTheme.typography.titleLarge,
        )

        Text(getString(Res.string.food_search_empty_description))

        TextButton(onClick = { onIntent(FoodSearchIntent.Create) }) {
            Text(getString(Res.string.food_create))
        }
    }
}