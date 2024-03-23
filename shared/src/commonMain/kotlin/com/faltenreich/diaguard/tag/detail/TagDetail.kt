package com.faltenreich.diaguard.tag.detail

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.Divider
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.tag.EntryTag

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
                    input = viewModel.name.collectAsState().value,
                    onInputChange = { input -> viewModel.name.value = input },
                    label = getString(MR.strings.name),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = { Text(state?.inputError ?: "") },
                    isError = state?.inputError != null,
                )
            }
        }
        item { Divider(orientation = Orientation.Vertical) }
        items(
            items = state?.entryTags ?: emptyList(),
            key = EntryTag::id,
        ) { entryTag ->
            EntryListItem(
                entry = entryTag.entry,
                onClick = { viewModel.dispatchIntent(TagDetailIntent.OpenEntry(entryTag.entry)) },
            )
            Divider(orientation = Orientation.Vertical)
        }
    }
}