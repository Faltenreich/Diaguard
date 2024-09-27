package com.faltenreich.diaguard.tag.detail

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.entry.tag.EntryTag
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.name

@Composable
fun TagDetail(
    modifier: Modifier = Modifier,
    viewModel: TagDetailViewModel = inject(),
) {
    val state = viewModel.collectState()

    LazyColumn(modifier = modifier) {
        stickyHeader {
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
        }
        item { Divider(orientation = Orientation.Vertical) }
        items(
            items = state?.entryTags ?: emptyList(),
            key = EntryTag.Local::id,
        ) { entryTag ->
            Column(modifier = Modifier.animateItem()) {
                EntryListItem(
                    entry = entryTag.entry,
                    onClick = { viewModel.dispatchIntent(TagDetailIntent.OpenEntry(entryTag.entry)) },
                    onTagClick = { tag ->
                        viewModel.dispatchIntent(TagDetailIntent.OpenEntrySearch(query = tag.name))
                    },
                )
                Divider(orientation = Orientation.Vertical)
            }
        }
    }
}