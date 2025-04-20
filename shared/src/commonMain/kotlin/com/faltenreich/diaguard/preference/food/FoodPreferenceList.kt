package com.faltenreich.diaguard.preference.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.NoticeBar
import com.faltenreich.diaguard.shared.view.NoticeBarStyle
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_preferences_none_selected
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodPreferenceList(
    viewModel: FoodPreferenceListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(state.preferences) { preference ->
                preference.Content(Modifier)
            }
        }
        NoticeBar(
            text = stringResource(Res.string.food_preferences_none_selected),
            isVisible = !state.showAnyFood,
            style = NoticeBarStyle.WARNING,
        )
    }
}