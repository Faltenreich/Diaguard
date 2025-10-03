package com.faltenreich.diaguard.log.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.log.LogIntent
import com.faltenreich.diaguard.log.list.item.LogDayStyle
import com.faltenreich.diaguard.log.list.item.LogEmpty
import com.faltenreich.diaguard.log.list.item.LogEntry
import com.faltenreich.diaguard.log.list.item.LogItemState
import com.faltenreich.diaguard.log.list.item.LogLoadingIndicator
import com.faltenreich.diaguard.log.list.item.LogMonth
import com.faltenreich.diaguard.shared.view.Skeleton
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LogList(
    state: LazyListState,
    items: LazyPagingItems<LogItemState>,
    onIntent: (LogIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        state = state,
    ) {
        if (items.loadState.prepend == LoadState.Loading) {
            item {
                LogLoadingIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        for (index in 0 until items.itemCount) {
            when (val peek = items.peek(index)) {
                is LogItemState.MonthHeader -> item(key = peek.key) {
                    LogMonth(items[index] as LogItemState.MonthHeader)
                }

                is LogItemState.EntryContent -> item(key = peek.key) {
                    val item = items[index] as LogItemState.EntryContent
                    LogEntry(
                        state = item,
                        onClick = { onIntent(LogIntent.OpenEntry(item.entryState.entry)) },
                        onTagClick = { tag -> onIntent(LogIntent.OpenEntrySearch(tag.name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = AppTheme.dimensions.padding.P_3,
                                vertical = AppTheme.dimensions.padding.P_2,
                            ),
                    )
                }

                is LogItemState.EmptyContent -> item(key = peek.key) {
                    LogEmpty(
                        state = items[index] as LogItemState.EmptyContent,
                        onIntent = onIntent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = AppTheme.dimensions.padding.P_3),
                    )
                }

                null -> item {
                    Skeleton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppTheme.dimensions.size.TouchSizeMedium),
                    )
                }
            }
        }

        if (items.loadState.append == LoadState.Loading) {
            item {
                LogLoadingIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    val dateTime = now()
    val date = dateTime.date
    LogList(
        state = rememberLazyListState(),
        items = flowOf(
            PagingData.from(
                listOf(
                    LogItemState.MonthHeader(
                        date = date,
                        dateLocalized = date.toString(),
                    ),
                    LogItemState.EmptyContent(
                        date = date,
                        style = LogDayStyle(
                            isVisible = true,
                            isHighlighted = true,
                        ),
                    ),
                    LogItemState.EntryContent(
                        entryState = EntryListItemState(
                            entry = entry(),
                            dateTimeLocalized = dateTime.toString(),
                            foodEatenLocalized = emptyList(),
                            categories = emptyList(),
                        ),
                        style = LogDayStyle(
                            isVisible = true,
                            isHighlighted = true,
                        ),
                    ),
                ),
            ),
        ).collectAsLazyPagingItems(),
        onIntent = {},
    )
}