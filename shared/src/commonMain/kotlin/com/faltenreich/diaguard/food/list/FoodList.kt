package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun FoodList(
    modifier: Modifier = Modifier,
    viewModel: FoodListViewModel = inject(),
) {
    when (val viewState = viewModel.viewState.collectAsState().value) {
        is FoodListViewState.Loading -> LoadingIndicator(modifier = modifier)
        is FoodListViewState.Result -> LazyColumn(modifier = modifier) {
            items(viewState.items, key = Food::id) { food ->
                Column {
                    FoodListItem(food)
                    Divider()
                }
            }
        }
    }
}