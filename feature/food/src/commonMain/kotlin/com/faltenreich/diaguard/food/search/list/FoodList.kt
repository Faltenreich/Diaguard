package com.faltenreich.diaguard.food.search.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.data.food.Food
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.view.divider.Divider
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FoodList(
    items: LazyPagingItems<Food.Localized>,
    onSelect: (Food.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(count = items.itemCount) { index ->
            val food = items[index]
            if (food != null) {
                Column {
                    FoodListItem(
                        food = food,
                        modifier = Modifier.clickable { onSelect(food.local) },
                    )
                    Divider()
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
private fun Preview() = PreviewScaffold {
    FoodList(
        items = flowOf(
            PagingData.from(
                listOf(food().localized()),
            ),
        ).collectAsLazyPagingItems(),
        onSelect = {},
    )
}