package com.faltenreich.diaguard.entry.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun EntryList(
    modifier: Modifier = Modifier,
    viewModel: EntryListViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    LazyColumn(modifier = modifier) {
        items(viewState.entries) { entry ->
            Text(entry.toString())
        }
    }
}