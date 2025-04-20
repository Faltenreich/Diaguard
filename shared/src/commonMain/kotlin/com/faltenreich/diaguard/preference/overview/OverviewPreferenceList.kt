package com.faltenreich.diaguard.preference.overview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.PreferenceList

@Composable
fun OverviewPreferenceList(
    viewModel: OverviewPreferenceListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()
    PreferenceList(
        items = state?.items ?: emptyList(),
        modifier = modifier,
    )
}