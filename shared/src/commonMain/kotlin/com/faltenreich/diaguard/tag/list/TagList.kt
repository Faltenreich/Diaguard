package com.faltenreich.diaguard.tag.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.tag.Tag

@Composable
fun TagList(
    viewModel: TagListViewModel,
    modifier: Modifier = Modifier,
) {
    when (val viewState = viewModel.collectState()) {
        null -> Unit
        else ->
            LazyColumn(modifier) {
                items(viewState.tags, key = Tag.Local::id) { tag ->
                    TagListItem(
                        tag = tag,
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth()
                            .clickable { viewModel.dispatchIntent(TagListIntent.OpenTag(tag)) },
                    )
                    Divider(orientation = Orientation.Vertical)
                }
            }
    }
}