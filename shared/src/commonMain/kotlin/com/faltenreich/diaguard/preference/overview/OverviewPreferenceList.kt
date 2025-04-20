package com.faltenreich.diaguard.preference.overview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OverviewPreferenceList(
    viewModel: OverviewPreferenceListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(state?.items ?: emptyList()) { preference ->
            preference.Content(Modifier)
        }
    }
}