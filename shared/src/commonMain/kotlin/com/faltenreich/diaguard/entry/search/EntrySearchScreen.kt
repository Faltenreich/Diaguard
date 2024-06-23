package com.faltenreich.diaguard.entry.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.ClearButton
import com.faltenreich.diaguard.shared.view.SearchField
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.entry_search_prompt
import diaguard.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parametersOf

data class EntrySearchScreen(val query: String = "") : Screen {

    override val bottomAppBarStyle: BottomAppBarStyle
        get() = BottomAppBarStyle.Visible(
            actions = {
                val viewModel = getViewModel<EntrySearchViewModel> { parametersOf(query) }
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

    @Composable
    override fun Content() {
        EntrySearch(viewModel = getViewModel { parametersOf(query) })
    }
}