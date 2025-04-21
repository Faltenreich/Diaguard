package com.faltenreich.diaguard.preference.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.item.PreferenceCategoryListItem2
import com.faltenreich.diaguard.preference.list.item.PreferenceCheckBoxListItem2
import com.faltenreich.diaguard.shared.view.NoticeBar
import com.faltenreich.diaguard.shared.view.NoticeBarStyle
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_custom
import diaguard.shared.generated.resources.food_custom_show
import diaguard.shared.generated.resources.food_custom_show_desc
import diaguard.shared.generated.resources.food_preferences_none_selected
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoodPreferenceList(
    viewModel: FoodPreferenceListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    Column(modifier = modifier) {
        LazyColumn(modifier = Modifier.weight(AppTheme.dimensions.weight.W_1)) {
            stickyHeader {
                PreferenceCategoryListItem2(title = stringResource(Res.string.food_custom))
            }
            item {
                PreferenceCheckBoxListItem2(
                    title = stringResource(Res.string.food_custom_show),
                    subtitle = stringResource(Res.string.food_custom_show_desc),
                    isChecked = state.showCustomFood,
                    onCheckedChange = { isChecked ->
                        viewModel.dispatchIntent(FoodPreferenceListIntent.SetShowCustomFood(isChecked))
                    },
                )
            }
        }
        NoticeBar(
            text = stringResource(Res.string.food_preferences_none_selected),
            isVisible = !state.showAnyFood,
            style = NoticeBarStyle.WARNING,
        )
    }
}