package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun FoodEatenList(
    modifier: Modifier = Modifier,
    viewModel: FoodEatenListViewModel,
) {
    when (val viewState = viewModel.collectState()) {
        null -> Unit
        is FoodEatenListViewState.Empty -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(getString(MR.strings.no_entries))
        }
        is FoodEatenListViewState.Loaded -> Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) {
            viewState.results.forEach { foodEaten ->
                FoodEatenListItem(foodEaten)
                Divider()
            }
        }
    }
}