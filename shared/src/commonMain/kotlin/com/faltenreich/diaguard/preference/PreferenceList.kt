package com.faltenreich.diaguard.preference

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject

@Composable
fun Preferences(
    modifier: Modifier = Modifier,
    viewModel: PreferenceListViewModel = inject(),
) {
    val viewState = viewModel
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {

    }
}