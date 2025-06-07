package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import diaguard.shared.generated.resources.ic_sad
import org.jetbrains.compose.resources.painterResource

@Composable
fun FoodListEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.padding.P_4),
        verticalArrangement = Arrangement.spacedBy(
            space = AppTheme.dimensions.padding.P_3,
            alignment = Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_sad),
            contentDescription = null,
            modifier = Modifier.size(AppTheme.dimensions.size.ImageXLarge),
        )
        Text(
            text = getString(Res.string.food_search_empty_title),
            fontWeight = FontWeight.Bold,
        )
        Text(getString(Res.string.food_search_empty_description))
    }
}