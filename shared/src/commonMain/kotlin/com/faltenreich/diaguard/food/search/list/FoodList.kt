package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodList(
    items: LazyPagingItems<Food.Local>,
    onSelect: (Food.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        for (index in 0 until items.itemCount) {
            item {
                val food = items[index]
                if (food != null) {
                    Column {
                        FoodListItem(
                            food = food,
                            modifier = Modifier.clickable { onSelect(food) },
                        )
                        Divider()
                    }
                }
            }
        }

        if (items.loadState.append == LoadState.Loading) {
            item {
                FoodListLoadingIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    FoodList(
        items = flowOf(PagingData.from(listOf(food()))).collectAsLazyPagingItems(),
        onSelect = {},
    )
}