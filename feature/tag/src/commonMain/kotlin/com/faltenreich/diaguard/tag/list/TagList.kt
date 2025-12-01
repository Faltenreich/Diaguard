package com.faltenreich.diaguard.tag.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.tag.form.TagFormDialog
import com.faltenreich.diaguard.view.divider.Divider
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TagList(
    state: TagListState?,
    onIntent: (TagListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

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
                        .clickable { onIntent(TagListIntent.OpenTag(tag)) },
                )
            }
        }
    }

    state.formDialog?.let { formDialog ->
        TagFormDialog(
            error = formDialog.error,
            onDismissRequest = { onIntent(TagListIntent.CloseFormDialog) },
            onConfirmRequest = { name -> onIntent(TagListIntent.StoreTag(name = name)) },
        )
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    TagList(
        state = TagListState(
            tags = listOf(tag()),
            formDialog = null,
        ),
        onIntent = {},
    )
}