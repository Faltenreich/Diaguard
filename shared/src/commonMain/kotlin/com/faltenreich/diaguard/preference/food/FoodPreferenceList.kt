package com.faltenreich.diaguard.preference.food

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.PreferenceList
import com.faltenreich.diaguard.shared.view.NoticeBar
import com.faltenreich.diaguard.shared.view.NoticeBarStyle
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_preferences_none_selected
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodPreferenceList(
    modifier: Modifier = Modifier,
    viewModel: FoodPreferenceListViewModel,
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        PreferenceList(
            items = state.preferences,
            modifier = Modifier.weight(AppTheme.dimensions.weight.W_1),
        )
        NoticeBar(
            text = stringResource(Res.string.food_preferences_none_selected),
            isVisible = !state.showAnyFood,
            style = NoticeBarStyle.WARNING,
        )
    }
}