package com.faltenreich.diaguard.food.eaten.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.no_entries

@Composable
fun FoodEatenList(
    modifier: Modifier = Modifier,
    viewModel: FoodEatenListViewModel,
) {
    when (val state = viewModel.collectState()) {
        null -> Unit
        is FoodEatenListState.Empty -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(getString(Res.string.no_entries))
        }
        is FoodEatenListState.NonEmpty -> Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) {
            state.results.forEach { foodEaten ->
                FoodEatenListItem(
                    foodEaten = foodEaten,
                    onIntent = viewModel::dispatchIntent,
                )
                Divider()
            }
        }
    }
}