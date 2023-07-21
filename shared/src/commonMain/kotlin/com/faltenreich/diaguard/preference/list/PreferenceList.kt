package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.item.PlainPreferenceItem
import com.faltenreich.diaguard.preference.list.item.SelectPreferenceItem
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun PreferenceList(
    modifier: Modifier = Modifier,
    viewModel: PreferenceListViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(viewState.listItems) { preference ->
            when (preference) {
                is Preference.Plain -> PlainPreferenceItem(preference)
                is Preference.Selection<*> -> SelectPreferenceItem(preference)
            }
        }
    }
}