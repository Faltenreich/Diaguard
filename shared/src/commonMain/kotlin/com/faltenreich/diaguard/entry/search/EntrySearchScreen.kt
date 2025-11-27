package com.faltenreich.diaguard.entry.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.data.navigation.BottomAppBarStyle
import com.faltenreich.diaguard.data.navigation.TopAppBarStyle
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.entry_search
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

@Serializable
data class EntrySearchScreen(private val query: String) : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.entry_search))
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