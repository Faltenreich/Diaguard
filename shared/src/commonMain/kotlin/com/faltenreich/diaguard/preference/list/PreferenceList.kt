package com.faltenreich.diaguard.preference.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun PreferenceList(
    modifier: Modifier = Modifier,
    viewModel: PreferenceListViewModel = inject(),
) {
    val items = viewModel.collectState()?.items ?: return
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items) { preference ->
            preference.Content(Modifier)
        }
    }
}