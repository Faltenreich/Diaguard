package com.faltenreich.diaguard.food.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import com.faltenreich.diaguard.food.Food
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun FoodList(
    items: LazyPagingItems<Food.Local>,
    onRefresh: () -> Unit,
    onSelect: (Food.Local) -> Unit,
    modifier: Modifier = Modifier,
) {
    // FIXME
    var isRefreshing by rememberSaveable { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn {
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
}