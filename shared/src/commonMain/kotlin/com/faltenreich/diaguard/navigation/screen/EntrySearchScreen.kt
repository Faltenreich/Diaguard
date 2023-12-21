package com.faltenreich.diaguard.navigation.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.entry.search.EntrySearchViewModel
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.SearchField
import org.koin.core.parameter.parametersOf

data class EntrySearchScreen(val query: String = "") : Screen {

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<EntrySearchViewModel> { parametersOf(query) }
                SearchField(
                    query = viewModel.query,
                    placeholder = getString(MR.strings.entry_search_prompt),
                    onQueryChange = { query -> viewModel.query = query },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = AppTheme.dimensions.padding.P_3),
                )
            }
        )

    @Composable
    override fun Content() {
        EntrySearch(viewModel = getViewModel { parametersOf(query) })
    }
}