package com.faltenreich.diaguard.tag.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.tag.Tag
import com.faltenreich.diaguard.tag.form.TagFormDialog

@Composable
fun TagList(
    modifier: Modifier = Modifier,
    viewModel: TagListViewModel = inject(),
) {
    when (val viewState = viewModel.viewState.collectAsState().value) {
        is TagListViewState.Loading -> Unit
        is TagListViewState.Loaded -> Box {
            LazyColumn(modifier) {
                items(viewState.tags, key = Tag::id) { tag ->
                    TagListItem(tag)
                    Divider()
                }
            }
            if (viewState.showFormDialog) {
                TagFormDialog(
                    onDismissRequest = { viewModel.handleIntent(TagListIntent.CloseForm) },
                    onConfirmRequest = { name -> viewModel.handleIntent(TagListIntent.Submit(name)) }
                )
            }
        }
    }
}