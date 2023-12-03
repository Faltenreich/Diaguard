package com.faltenreich.diaguard.tag.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag

@Composable
fun TagList(
    modifier: Modifier = Modifier,
    viewModel: TagListViewModel = inject(),
) {
    when (val viewState = viewModel.viewState.collectAsState().value) {
        is TagListViewState.Loading -> Unit
        is TagListViewState.Loaded -> LazyColumn(modifier) {
            items(viewState.tags, key = Tag::id) { tag ->
                TagListItem(tag)
            }
        }
    }
}