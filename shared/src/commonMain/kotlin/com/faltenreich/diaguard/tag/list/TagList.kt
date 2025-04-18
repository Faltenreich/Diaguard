package com.faltenreich.diaguard.tag.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.tag.form.TagFormDialog

@Composable
fun TagList(
    viewModel: TagListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    LazyColumn(modifier) {
        itemsIndexed(state.tags, key = { _, item -> item.id }) { index, tag ->
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

    when (state.form) {
        is TagListState.Form.Hidden -> Unit
        is TagListState.Form.Shown -> TagFormDialog(
            error = state.form.error,
            onDismissRequest = { viewModel.dispatchIntent(TagListIntent.CloseFormDialog) },
            onConfirmRequest = { name -> viewModel.dispatchIntent(TagListIntent.StoreTag(name = name)) }
        )
    }
}