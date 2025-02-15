package com.faltenreich.diaguard.tag.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.ResourceIcon
import com.faltenreich.diaguard.shared.view.TextDivider
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entries
import diaguard.shared.generated.resources.entry_search_empty
import diaguard.shared.generated.resources.ic_tag
import diaguard.shared.generated.resources.name

@Composable
fun TagDetail(
    viewModel: TagDetailViewModel,
    modifier: Modifier = Modifier,
) {
    val items = viewModel.pagingData.collectAsLazyPagingItems()

    Column(modifier = modifier) {
        Card(shape = RectangleShape) {
            FormRow(icon = { ResourceIcon(Res.drawable.ic_tag) }) {
                TextInput(
                    input = viewModel.name,
                    onInputChange = { input -> viewModel.name = input },
                    label = getString(Res.string.name),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = viewModel.error?.let { error -> { Text(error) } },
                    isError = viewModel.error != null,
                )
            }
        }

        TextDivider(getString(Res.string.entries))

        EntryList(
            items = items,
            emptyContent = { Text(getString(Res.string.entry_search_empty)) },
            onEntryClick = { entry -> viewModel.dispatchIntent(TagDetailIntent.OpenEntry(entry)) },
            onTagClick = { tag -> viewModel.dispatchIntent(TagDetailIntent.OpenEntrySearch(query = tag.name)) },
        )
    }
}