package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun PreferenceList(
    modifier: Modifier = Modifier,
    viewModel: PreferenceListViewModel = inject(),
) {
    when (val viewState = viewModel.viewState.collectAsState().value) {
        is PreferenceListViewState.Loading -> LoadingIndicator()
        is PreferenceListViewState.Loaded -> LazyColumn(modifier = modifier.fillMaxSize()) {
            items(viewState.listItems) { preference ->
                preference.Content(Modifier)
            }
        }
    }
}