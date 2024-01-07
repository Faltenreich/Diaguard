package com.faltenreich.diaguard.tag.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.list.EntryListItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FormRow
import com.faltenreich.diaguard.shared.view.TextInput
import com.faltenreich.diaguard.tag.EntryTag

@Composable
fun TagDetail(
    modifier: Modifier = Modifier,
    viewModel: TagDetailViewModel = inject(),
) {
    val state = viewModel.collectState()
    var name by rememberSaveable { mutableStateOf("") }

    LazyColumn(modifier = modifier) {
        stickyHeader {
            FormRow {
                TextInput(
                    input = name,
                    onInputChange = { name = it },
                    label = getString(MR.strings.name),
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = { Text(state?.inputError ?: "") },
                    isError = state?.inputError != null,
                )
            }
        }
        item { Divider() }
        items(
            items = state?.entryTags ?: emptyList(),
            key = EntryTag::id,
        ) { entryTag ->
            EntryListItem(entry = entryTag.entry)
            Divider()
        }
    }
}