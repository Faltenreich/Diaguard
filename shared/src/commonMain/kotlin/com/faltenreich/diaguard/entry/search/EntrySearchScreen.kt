package com.faltenreich.diaguard.entry.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
                EntrySearchField(
                    query = viewModel.query,
                    onQueryChange = { viewModel.query = it },
                )
            }
        )
    }

    @Composable
    override fun Content() {
        EntrySearch(viewModel = viewModel(parameters = { parametersOf(query) }))
    }
}