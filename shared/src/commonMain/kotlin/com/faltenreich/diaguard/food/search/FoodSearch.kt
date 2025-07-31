package com.faltenreich.diaguard.food.search

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.food.search.list.FoodList
import com.faltenreich.diaguard.food.search.list.FoodListEmpty
import com.faltenreich.diaguard.food.search.list.FoodListSkeleton
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodSearch(
    state: FoodSearchState?,
    onSelect: (Food.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = state?.pagingData?.collectAsLazyPagingItems()

    Column(modifier = modifier) {
        FoodSearchHeader()
        
        if (items == null || items.loadState.refresh == LoadState.Loading) {
            FoodListSkeleton()
        } else {
            val isAtTheEnd = items.loadState.refresh !is LoadState.Loading
                && items.loadState.prepend !is LoadState.Loading
                && items.loadState.append !is LoadState.Loading
            if (isAtTheEnd && items.itemCount == 0) {
                FoodListEmpty()
            } else {
                FoodList(
                    items = items,
                    onSelect = onSelect,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodSearch(
        state = FoodSearchState(
            query = "Query",
            pagingData = flowOf(PagingData.from(listOf(food().localized()))),
        ),
        onSelect = {},
    )
}