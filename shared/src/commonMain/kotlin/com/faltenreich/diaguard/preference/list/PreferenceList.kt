package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.item.ActionPreferenceItem
import com.faltenreich.diaguard.preference.list.item.CategoryPreferenceListItem
import com.faltenreich.diaguard.preference.list.item.FolderPreferenceListItem
import com.faltenreich.diaguard.preference.list.item.ListPreferenceItem
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.LoadingIndicator

@Composable
fun PreferenceList(
    modifier: Modifier = Modifier,
    viewModel: PreferenceListViewModel = inject(),
) {
    when (val viewState = viewModel.viewState.collectAsState().value) {
        is PreferenceListViewState.Loading -> LoadingIndicator()
        is PreferenceListViewState.Loaded -> LazyColumn(modifier = modifier.fillMaxSize()) {
            items(viewState.listItems) { preference ->
                when (preference) {
                    is Preference.Folder -> FolderPreferenceListItem(preference)
                    is Preference.Category -> CategoryPreferenceListItem(preference)
                    is Preference.Action -> ActionPreferenceItem(preference)
                    is Preference.List -> ListPreferenceItem(preference)
                }
            }
        }
    }
}