package com.faltenreich.diaguard.tag.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.entry.list.EntryListItemState
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.DeleteDialog
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entries
import diaguard.shared.generated.resources.entry_search_empty
import diaguard.shared.generated.resources.ic_tag
import diaguard.shared.generated.resources.name
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TagDetail(
    state: TagDetailState?,
    entries: LazyPagingItems<EntryListItemState>,
    onIntent: (TagDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    var name by remember { mutableStateOf(state.name) }

    Column(modifier = modifier) {
        Card(shape = RectangleShape) {
            FormRow(icon = { ResourceIcon(Res.drawable.ic_tag) }) {
                TextInput(
                    input = name,
                    onInputChange = { input ->
                        onIntent(TagDetailIntent.SetName(input))
                        name = input
                    },
                    label = getString(Res.string.name),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = state.error?.let { error -> { Text(error) } },
                    isError = state.error != null,
                )
            }
        }

        TextDivider(getString(Res.string.entries))

        EntryList(
            items = entries,
            emptyContent = { Text(getString(Res.string.entry_search_empty)) },
            onEntryClick = { entry -> onIntent(TagDetailIntent.OpenEntry(entry)) },
            onEntryDelete = { entry -> onIntent(TagDetailIntent.DeleteEntry(entry)) },
            onEntryRestore = { entry -> onIntent(TagDetailIntent.RestoreEntry(entry)) },
            onTagClick = { tag -> onIntent(TagDetailIntent.OpenEntrySearch(query = tag.name)) },
        )
    }

    if (state.deleteDialog != null) {
        DeleteDialog(
            onDismissRequest = { onIntent(TagDetailIntent.CloseDeleteDialog) },
            onConfirmRequest = {
                onIntent(TagDetailIntent.CloseDeleteDialog)
                onIntent(TagDetailIntent.DeleteTag)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() = AppPreview {
    TagDetail(
        state = TagDetailState(
            name = tag().name,
            error = null,
            deleteDialog = null,
        ),
        entries = flowOf(
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
                                        valueLocalized = value().value.toString(),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        ).collectAsLazyPagingItems(),
        onIntent = {},
    )
}