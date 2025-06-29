package com.faltenreich.diaguard.entry.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_search
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf

@Serializable
data class EntrySearchScreen(private val query: String = "") : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.entry_search))
        }
    }

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<EntrySearchViewModel>(parameters = { parametersOf(query) })
        return BottomAppBarStyle.Visible(
            actions = {
                // FIXME: Not synchronized with EntrySearch, e.g. onTagClick
                var query by remember { mutableStateOf(query) }
                EntrySearchField(
                    query = query,
                    onQueryChange = { input ->
                        query = input
                        viewModel.dispatchIntent(EntrySearchIntent.SetQuery(input))
                    },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<EntrySearchViewModel>(parameters = { parametersOf(query) })
        EntrySearch(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}