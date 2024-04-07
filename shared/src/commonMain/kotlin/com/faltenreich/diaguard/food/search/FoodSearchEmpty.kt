package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_search_empty_description
import diaguard.shared.generated.resources.food_search_empty_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodSearchEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = AppTheme.dimensions.padding.P_2,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.food_search_empty_title),
            style = AppTheme.typography.titleLarge,
        )
        Text(stringResource(Res.string.food_search_empty_description))
    }
}