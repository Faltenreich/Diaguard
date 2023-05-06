package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.rememberViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.SearchField
import dev.icerock.moko.resources.compose.stringResource

class EntrySearch(private val query: String? = null) : Screen {

    @Composable
    override fun Content() {
        val viewModel = rememberViewModel { EntrySearchViewModel(query, inject(), inject()) }
        val state = viewModel.viewState.collectAsState().value
        Box(
            modifier = Modifier.padding(AppTheme.dimensions.padding.P_3),
        ) {
            SearchField(
                query = state.query,
                placeholder = stringResource(MR.strings.search_placeholder),
                onQueryChange = viewModel::onQueryChange,
            )
        }
    }
}