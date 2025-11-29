package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_search_empty
import diaguard.shared.generated.resources.entry_search_placeholder
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EntrySearch(
    state: EntrySearchState?,
    onIntent: (EntrySearchIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    EntryList(
        items = state.pagingData.collectAsLazyPagingItems(),
        emptyContent = {
            if (state.query.isBlank()) {
                Text(getString(Res.string.entry_search_placeholder))
            } else {
                Text(getString(Res.string.entry_search_empty))
            }
        },
        onEntryClick = { entry ->
            onIntent(EntrySearchIntent.OpenEntry(entry))
        },
        onEntryDelete = { entry ->
            onIntent(EntrySearchIntent.DeleteEntry(entry))
        },
        onEntryRestore = { entry ->
            onIntent(EntrySearchIntent.RestoreEntry(entry))
        },
        onTagClick = { tag ->
            onIntent(EntrySearchIntent.SetQuery(tag.name))
            scope.launch { listState.scrollToItem(0) }
        },
        listState = listState,
        modifier = modifier.fillMaxSize(),
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    EntrySearch(
        state = EntrySearchState(
            query = "Query",
            pagingData = flowOf(
                PagingData.from(
                    listOf(
                        EntryListItemState(
                            entry = entry(),
                            dateTimeLocalized = now().toString(),
                            foodEatenLocalized = emptyList(),
                            categories = listOf(
                                EntryListItemState.Category(
                                    category = category(),
                                    values = listOf(
                                        EntryListItemState.Value(
                                            property = property(),
                                            value = value(),
                                            valueLocalized = value().value.toString(),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        ),
        onIntent = {},
    )
}