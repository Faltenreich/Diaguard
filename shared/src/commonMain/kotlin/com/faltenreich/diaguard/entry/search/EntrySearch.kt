package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.view.SearchField
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EntrySearch(
    viewModel: EntrySearchViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.viewState.collectAsState().value
    Box(
        modifier = modifier.padding(16.dp),
    ) {
        SearchField(
            query = state.query,
            placeholder = stringResource(MR.strings.search_placeholder),
            onQueryChange = viewModel::onQueryChange,
        )
    }
}