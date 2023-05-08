package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.list.EntryList
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.rememberViewModel
import com.faltenreich.diaguard.shared.view.SearchField
import dev.icerock.moko.resources.compose.stringResource

class EntrySearch(private val query: String? = null) : Screen {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = {
            val viewModel = rememberViewModel { EntrySearchViewModel(query) }
            val state = viewModel.viewState.collectAsState().value
            SearchField(
                query = state.query,
                placeholder = stringResource(MR.strings.search_placeholder),
                onQueryChange = viewModel::onQueryChange,
            )
        }
    )

    @Composable
    override fun Content() {
        val viewModel = rememberViewModel { EntrySearchViewModel(query) }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (val viewState = viewModel.viewState.collectAsState().value) {
                is EntrySearchViewState.Idle ->  Text(stringResource(MR.strings.entry_search_placeholder))
                is EntrySearchViewState.Loading -> CircularProgressIndicator()
                is EntrySearchViewState.Result -> EntryList(viewState.entries)
                is EntrySearchViewState.Error -> Text("Error")
            }
        }
    }
}