package com.faltenreich.diaguard.preference.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.list.item.PreferenceActionListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceCategoryListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceCheckBoxListItem
import com.faltenreich.diaguard.shared.view.NoticeBar
import com.faltenreich.diaguard.shared.view.NoticeBarStyle
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food_branded
import diaguard.shared.generated.resources.food_branded_show
import diaguard.shared.generated.resources.food_branded_show_desc
import diaguard.shared.generated.resources.food_common
import diaguard.shared.generated.resources.food_common_show
import diaguard.shared.generated.resources.food_common_show_desc
import diaguard.shared.generated.resources.food_custom
import diaguard.shared.generated.resources.food_custom_show
import diaguard.shared.generated.resources.food_custom_show_desc
import diaguard.shared.generated.resources.food_preferences_none_selected
import diaguard.shared.generated.resources.food_source_branded_provider
import diaguard.shared.generated.resources.food_source_branded_url
import diaguard.shared.generated.resources.food_source_common_provider
import diaguard.shared.generated.resources.food_source_common_url
import diaguard.shared.generated.resources.food_source_powered_by
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
                PreferenceCategoryListItem(title = stringResource(Res.string.food_custom))
            }
            item {
                PreferenceCheckBoxListItem(
                    title = stringResource(Res.string.food_custom_show),
                    subtitle = stringResource(Res.string.food_custom_show_desc),
                    isChecked = state.showCustomFood,
                    onCheckedChange = { isChecked ->
                        viewModel.dispatchIntent(FoodPreferenceListIntent.SetShowCustomFood(isChecked))
                    },
                )
            }

            stickyHeader {
                PreferenceCategoryListItem(title = stringResource(Res.string.food_common))
            }
            item {
                val url = stringResource(Res.string.food_source_common_url)
                PreferenceActionListItem(
                    title = stringResource(Res.string.food_source_common_provider),
                    subtitle = stringResource(Res.string.food_source_powered_by),
                    onClick = {
                        viewModel.dispatchIntent(FoodPreferenceListIntent.OpenUrl(url))
                    },
                )
            }
            item {
                PreferenceCheckBoxListItem(
                    title = stringResource(Res.string.food_common_show),
                    subtitle = stringResource(Res.string.food_common_show_desc),
                    isChecked = state.showCommonFood,
                    onCheckedChange = { isChecked ->
                        viewModel.dispatchIntent(FoodPreferenceListIntent.SetShowCommonFood(isChecked))
                    },
                )
            }

            stickyHeader {
                PreferenceCategoryListItem(title = stringResource(Res.string.food_branded))
            }
            item {
                val url = stringResource(Res.string.food_source_branded_url)
                PreferenceActionListItem(
                    title = stringResource(Res.string.food_source_branded_provider),
                    subtitle = stringResource(Res.string.food_source_powered_by),
                    onClick = {
                        viewModel.dispatchIntent(FoodPreferenceListIntent.OpenUrl(url))
                    },
                )
            }
            item {
                PreferenceCheckBoxListItem(
                    title = stringResource(Res.string.food_branded_show),
                    subtitle = stringResource(Res.string.food_branded_show_desc),
                    isChecked = state.showBrandedFood,
                    onCheckedChange = { isChecked ->
                        viewModel.dispatchIntent(FoodPreferenceListIntent.SetShowBrandedFood(isChecked))
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