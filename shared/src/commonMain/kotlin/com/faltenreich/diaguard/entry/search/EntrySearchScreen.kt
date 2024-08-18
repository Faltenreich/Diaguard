package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.navigation.bar.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.ClearButton
import com.faltenreich.diaguard.shared.view.SearchField
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_search_prompt
import diaguard.shared.generated.resources.ic_search
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

@Serializable
data class EntrySearchScreen(private val query: String = "") : Screen {

    @Composable
    override fun BottomAppBar(): BottomAppBarStyle {
        val viewModel = viewModel<EntrySearchViewModel>(parameters = { parametersOf(query) })
        return BottomAppBarStyle.Visible(
            actions = {
                SearchField(
                    query = viewModel.query,
                    placeholder = getString(Res.string.entry_search_prompt),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_search),
                            contentDescription = null,
                        )
                    },
                    trailingIcon = { ClearButton(onClick = { viewModel.query = "" }) },
                    onQueryChange = { query -> viewModel.query = query },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = AppTheme.dimensions.padding.P_3),
                )
            }
        )
    }

    @Composable
    override fun Content() {
        EntrySearch(viewModel = viewModel(parameters = { parametersOf(query) }))
    }
}