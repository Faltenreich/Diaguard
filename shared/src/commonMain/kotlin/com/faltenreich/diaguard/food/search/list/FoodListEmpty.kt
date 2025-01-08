package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_search_empty_description
import diaguard.shared.generated.resources.food_search_empty_title

@Composable
fun FoodListEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.padding.P_3),
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
    }
}