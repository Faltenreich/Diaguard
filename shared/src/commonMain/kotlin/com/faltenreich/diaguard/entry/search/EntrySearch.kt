package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.SearchField
import dev.icerock.moko.resources.compose.stringResource

class EntrySearch(private val query: String? = null) : NavigationTarget {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { EntrySearchViewModel(query, inject(), inject()) }
        val state = viewModel.viewState.collectAsState().value
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            SearchField(
                query = state.query,
                placeholder = stringResource(MR.strings.search_placeholder),
                onQueryChange = viewModel::onQueryChange,
            )
        }
    }
}