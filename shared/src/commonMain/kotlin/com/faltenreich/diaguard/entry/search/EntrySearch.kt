package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun EntrySearch(
    modifier: Modifier = Modifier,
    viewModel: EntrySearchViewModel = inject(),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val viewState = viewModel.viewState.collectAsState().value) {
            is EntrySearchViewState.Idle ->  Text(getString(MR.strings.entry_search_placeholder))
            is EntrySearchViewState.Loading -> LoadingIndicator()
            is EntrySearchViewState.Result -> EntryList(viewState.items ?: emptyList())
        }
    }
}