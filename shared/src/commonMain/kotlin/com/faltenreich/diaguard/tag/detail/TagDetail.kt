package com.faltenreich.diaguard.tag.detail

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.name

@Composable
fun TagDetail(
    viewModel: TagDetailViewModel,
    modifier: Modifier = Modifier,
) {
    val items = viewModel.pagingData.collectAsLazyPagingItems()

    Column(modifier = modifier) {
        FormRow {
            TextInput(
                input = viewModel.name,
                onInputChange = { input -> viewModel.name = input },
                label = getString(Res.string.name),
                modifier = Modifier.fillMaxWidth(),
                supportingText = viewModel.error?.let { error -> { Text(error) } },
                isError = viewModel.error != null,
            )
        }

        Divider(orientation = Orientation.Vertical)

        EntryList(
            items = items,
            onEntryClick = { entry -> viewModel.dispatchIntent(TagDetailIntent.OpenEntry(entry)) },
            onTagClick = { tag -> viewModel.dispatchIntent(TagDetailIntent.OpenEntrySearch(query = tag.name)) },
        )
    }
}