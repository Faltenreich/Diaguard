package com.faltenreich.diaguard.preference.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.preference.list.PreferenceActionListItem
import com.faltenreich.diaguard.preference.list.PreferenceCategoryListItem
import com.faltenreich.diaguard.preference.list.PreferenceCheckBoxListItem
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.food_branded
import com.faltenreich.diaguard.resource.food_branded_show
import com.faltenreich.diaguard.resource.food_branded_show_desc
import com.faltenreich.diaguard.resource.food_common
import com.faltenreich.diaguard.resource.food_common_show
import com.faltenreich.diaguard.resource.food_common_show_desc
import com.faltenreich.diaguard.resource.food_custom
import com.faltenreich.diaguard.resource.food_custom_show
import com.faltenreich.diaguard.resource.food_custom_show_desc
import com.faltenreich.diaguard.resource.food_preferences_none_selected
import com.faltenreich.diaguard.resource.food_source_branded_provider
import com.faltenreich.diaguard.resource.food_source_branded_url
import com.faltenreich.diaguard.resource.food_source_common_provider
import com.faltenreich.diaguard.resource.food_source_common_url
import com.faltenreich.diaguard.resource.food_source_powered_by
import com.faltenreich.diaguard.view.info.NoticeBar
import com.faltenreich.diaguard.view.info.NoticeBarStyle
import com.faltenreich.diaguard.view.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun FoodPreferenceList(
    state: FoodPreferenceListState?,
    onIntent: (FoodPreferenceListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    Column(modifier = modifier) {
        LazyColumn(modifier = Modifier.weight(AppTheme.dimensions.weight.W_1)) {
            stickyHeader {
                PreferenceCategoryListItem(
                    title = stringResource(Res.string.food_custom)
                )
            }
            item {
                PreferenceCheckBoxListItem(
                    title = stringResource(Res.string.food_custom_show),
                    subtitle = stringResource(Res.string.food_custom_show_desc),
                    isChecked = state.showCustomFood,
                    onCheckedChange = { isChecked ->
                        onIntent(
                            FoodPreferenceListIntent.SetShowCustomFood(
                                isChecked
                            )
                        )
                    },
                )
            }

            stickyHeader {
                PreferenceCategoryListItem(
                    title = stringResource(Res.string.food_common)
                )
            }
            item {
                val url = stringResource(Res.string.food_source_common_url)
                PreferenceActionListItem(
                    title = stringResource(Res.string.food_source_common_provider),
                    subtitle = stringResource(Res.string.food_source_powered_by),
                    onClick = {
                        onIntent(
                            FoodPreferenceListIntent.OpenUrl(
                                url
                            )
                        )
                    },
                )
            }
            item {
                PreferenceCheckBoxListItem(
                    title = stringResource(Res.string.food_common_show),
                    subtitle = stringResource(Res.string.food_common_show_desc),
                    isChecked = state.showCommonFood,
                    onCheckedChange = { isChecked ->
                        onIntent(
                            FoodPreferenceListIntent.SetShowCommonFood(
                                isChecked
                            )
                        )
                    },
                )
            }

            stickyHeader {
                PreferenceCategoryListItem(
                    title = stringResource(Res.string.food_branded)
                )
            }
            item {
                val url = stringResource(Res.string.food_source_branded_url)
                PreferenceActionListItem(
                    title = stringResource(Res.string.food_source_branded_provider),
                    subtitle = stringResource(Res.string.food_source_powered_by),
                    onClick = {
                        onIntent(
                            FoodPreferenceListIntent.OpenUrl(
                                url
                            )
                        )
                    },
                )
            }
            item {
                PreferenceCheckBoxListItem(
                    title = stringResource(Res.string.food_branded_show),
                    subtitle = stringResource(Res.string.food_branded_show_desc),
                    isChecked = state.showBrandedFood,
                    onCheckedChange = { isChecked ->
                        onIntent(
                            FoodPreferenceListIntent.SetShowBrandedFood(
                                isChecked
                            )
                        )
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

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    FoodPreferenceList(
        state = FoodPreferenceListState(
            showCustomFood = true,
            showCommonFood = false,
            showBrandedFood = true,
        ),
        onIntent = {},
    )
}