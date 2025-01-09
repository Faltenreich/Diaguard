package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.food.search.list.FoodList
import com.faltenreich.diaguard.food.search.list.FoodListEmpty
import com.faltenreich.diaguard.food.search.list.FoodListSkeleton
import com.faltenreich.diaguard.shared.view.LifecycleState
import com.faltenreich.diaguard.shared.view.rememberLifecycleState

@Composable
fun FoodSearch(
    modifier: Modifier = Modifier,
    viewModel: FoodSearchViewModel,
    selectionViewModel: FoodSelectionViewModel,
) {
    val state = viewModel.collectState() ?: return
    val items = state.pagingData.collectAsLazyPagingItems()

    val lifecycleState = rememberLifecycleState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == LifecycleState.RESUMED) {
            items.refresh()
        }
    }

    Column(modifier = modifier) {

        val isAtTheEnd = items.loadState.refresh !is LoadState.Loading
            && items.loadState.prepend !is LoadState.Loading
            && items.loadState.append !is LoadState.Loading
        
        if (items.loadState.refresh == LoadState.Loading) {
            FoodListSkeleton()
        } else if (isAtTheEnd && items.itemCount == 0) {
            FoodListEmpty()
        } else {
            FoodList(
                items = items,
                onSelect = { food ->
                    when (viewModel.mode) {
                        FoodSearchMode.STROLL -> viewModel.dispatchIntent(FoodSearchIntent.OpenFood(food))
                        FoodSearchMode.FIND -> {
                            selectionViewModel.postEvent(FoodSelectionEvent.Select(food))
                            viewModel.dispatchIntent(FoodSearchIntent.Close)
                        }
                    }
                },
            )
        }
    }
}