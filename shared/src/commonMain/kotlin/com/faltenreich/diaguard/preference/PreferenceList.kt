package com.faltenreich.diaguard.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun PreferenceList(
    modifier: Modifier = Modifier,
    viewModel: PreferenceListViewModel = inject(),
) {
    val viewState = viewModel.viewState.collectAsState().value
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        items(viewState.listItems) { preference ->
            when (preference) {
                is PreferenceListItem.Plain -> Column {
                    Text(preference.title)
                    preference.subtitle?.let { subtitle -> Text(subtitle) }
                }
            }
        }
    }
}