package com.faltenreich.diaguard.navigation.screen

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.entry.search.EntrySearch
import com.faltenreich.diaguard.shared.di.getViewModel
import org.koin.core.parameter.parametersOf

data class EntrySearchScreen(val query: String = "") : Screen() {

    @Composable
    override fun Content() {
        EntrySearch(viewModel = getViewModel { parametersOf(query) })
    }
}