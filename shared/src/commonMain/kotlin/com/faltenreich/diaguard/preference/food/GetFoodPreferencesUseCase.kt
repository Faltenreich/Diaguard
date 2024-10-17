package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.FoodPreference
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.list.item.preferences
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.networking.UrlOpener
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
import diaguard.shared.generated.resources.food_source_branded_provider
import diaguard.shared.generated.resources.food_source_branded_url
import diaguard.shared.generated.resources.food_source_common_provider
import diaguard.shared.generated.resources.food_source_common_url
import diaguard.shared.generated.resources.food_source_powered_by
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetFoodPreferencesUseCase(
    private val localization: Localization,
    private val urlOpener: UrlOpener,
    private val getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
) {

    operator fun invoke(): Flow<List<PreferenceListItem>> {
        return combine(
            getPreference(FoodPreference.ShowCustomFood),
            getPreference(FoodPreference.ShowCommonFood),
            getPreference(FoodPreference.ShowBrandedFood),
        ) { showCustomFood, showCommonFood, showBrandedFood ->
            preferences {
                category {
                    title = Res.string.food_custom
                }
                checkbox {
                    title = Res.string.food_custom_show
                    subtitle = localization.getString(Res.string.food_custom_show_desc)
                    isChecked = showCustomFood
                    onCheckedChange = { setPreference(FoodPreference.ShowCustomFood, it) }
                }
                category {
                    title = Res.string.food_common
                }
                action {
                    title = Res.string.food_source_common_provider
                    subtitle = localization.getString(Res.string.food_source_powered_by)
                    onClick =
                        { urlOpener.open(localization.getString(Res.string.food_source_common_url)) }
                }
                checkbox {
                    title = Res.string.food_common_show
                    subtitle = localization.getString(Res.string.food_common_show_desc)
                    isChecked = showCommonFood
                    onCheckedChange = { setPreference(FoodPreference.ShowCommonFood, it) }
                }
                category {
                    title = Res.string.food_branded
                }
                action {
                    title = Res.string.food_source_branded_provider
                    subtitle = localization.getString(Res.string.food_source_powered_by)
                    onClick =
                        { urlOpener.open(localization.getString(Res.string.food_source_branded_url)) }
                }
                checkbox {
                    title = Res.string.food_branded_show
                    subtitle = localization.getString(Res.string.food_branded_show_desc)
                    isChecked = showBrandedFood
                    onCheckedChange = { setPreference(FoodPreference.ShowBrandedFood, it) }
                }
            }
        }
    }
}