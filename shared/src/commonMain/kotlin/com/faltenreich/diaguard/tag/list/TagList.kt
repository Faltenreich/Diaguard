package com.faltenreich.diaguard.tag.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.Divider

@Composable
fun TagList(
    viewModel: TagListViewModel,
    modifier: Modifier = Modifier,
) {
    when (val viewState = viewModel.collectState()) {
        null -> Unit
        else ->
            LazyColumn(modifier) {
                itemsIndexed(viewState.tags, key = { _, item -> item.id }) { index, tag ->
                    Column {
                        if (index != 0) {
                            Divider()
                        }
                        TagListItem(
                            tag = tag,
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .clickable { viewModel.dispatchIntent(TagListIntent.OpenTag(tag)) },
                        )
                    }
                }
            }
    }
}