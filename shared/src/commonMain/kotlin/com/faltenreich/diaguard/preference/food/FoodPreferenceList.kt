package com.faltenreich.diaguard.preference.food

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.list.PreferenceList

@Composable
fun FoodPreferenceList(
    modifier: Modifier = Modifier,
    viewModel: FoodPreferenceListViewModel,
) {
    val items = viewModel.collectState() ?: emptyList()
    PreferenceList(
        items = items,
        modifier = modifier,
    )
}