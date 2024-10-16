package com.faltenreich.diaguard.preference.food

import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.list.item.preferences
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
import kotlinx.coroutines.flow.flowOf

class GetFoodPreferencesUseCase(
    private val localization: Localization,
    private val urlOpener: UrlOpener,
) {

    operator fun invoke(): Flow<List<PreferenceListItem>> {
        return flowOf(
            preferences {
                category {
                    title = Res.string.food_custom
                }
                checkbox {
                    title = Res.string.food_custom_show
                    subtitle = localization.getString(Res.string.food_custom_show_desc)
                    isChecked = true
                    onCheckedChange = { TODO() }
                }
                category {
                    title = Res.string.food_common
                }
                action {
                    title = Res.string.food_source_common_provider
                    subtitle = localization.getString(Res.string.food_source_powered_by)
                    onClick = { urlOpener.open(localization.getString(Res.string.food_source_common_url)) }
                }
                checkbox {
                    title = Res.string.food_common_show
                    subtitle = localization.getString(Res.string.food_common_show_desc)
                    isChecked = true
                    onCheckedChange = { TODO() }
                }
                category {
                    title = Res.string.food_branded
                }
                action {
                    title = Res.string.food_source_branded_provider
                    subtitle = localization.getString(Res.string.food_source_powered_by)
                    onClick = { urlOpener.open(localization.getString(Res.string.food_source_branded_url)) }
                }
                checkbox {
                    title = Res.string.food_branded_show
                    subtitle = localization.getString(Res.string.food_branded_show_desc)
                    isChecked = true
                    onCheckedChange = { TODO() }
                }
            }
        )
    }
}