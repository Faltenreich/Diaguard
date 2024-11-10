package com.faltenreich.diaguard.preference.overview

import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.navigation.bottomsheet.OpenBottomSheetUseCase
import com.faltenreich.diaguard.navigation.screen.PushScreenUseCase
import com.faltenreich.diaguard.preference.ColorScheme
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesFormScreen
import com.faltenreich.diaguard.preference.food.FoodPreferenceScreen
import com.faltenreich.diaguard.preference.license.LicenseListScreen
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceListListItem
import com.faltenreich.diaguard.preference.list.item.preferences
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.config.GetAppVersionUseCase
import com.faltenreich.diaguard.shared.localization.Localization
import com.faltenreich.diaguard.shared.networking.UrlOpener
import com.faltenreich.diaguard.tag.list.TagListScreen
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.about
import diaguard.shared.generated.resources.color_scheme
import diaguard.shared.generated.resources.contact
import diaguard.shared.generated.resources.data
import diaguard.shared.generated.resources.decimal_places
import diaguard.shared.generated.resources.facebook
import diaguard.shared.generated.resources.facebook_url
import diaguard.shared.generated.resources.facebook_url_short
import diaguard.shared.generated.resources.food
import diaguard.shared.generated.resources.homepage
import diaguard.shared.generated.resources.homepage_url
import diaguard.shared.generated.resources.homepage_url_short
import diaguard.shared.generated.resources.ic_about
import diaguard.shared.generated.resources.ic_contact
import diaguard.shared.generated.resources.ic_data
import diaguard.shared.generated.resources.licenses
import diaguard.shared.generated.resources.mail
import diaguard.shared.generated.resources.mail_url
import diaguard.shared.generated.resources.mail_url_short
import diaguard.shared.generated.resources.measurement_categories
import diaguard.shared.generated.resources.privacy_policy
import diaguard.shared.generated.resources.privacy_policy_url
import diaguard.shared.generated.resources.source_code
import diaguard.shared.generated.resources.source_code_url
import diaguard.shared.generated.resources.source_code_url_short
import diaguard.shared.generated.resources.start_screen
import diaguard.shared.generated.resources.tags
import diaguard.shared.generated.resources.terms_and_conditions
import diaguard.shared.generated.resources.terms_and_conditions_url
import diaguard.shared.generated.resources.version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetOverviewPreferencesUseCase(
    private val localization: Localization,
    private val urlOpener: UrlOpener,
    private val getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val getAppVersion: GetAppVersionUseCase,
    private val pushScreen: PushScreenUseCase,
    private val openBottomSheet: OpenBottomSheetUseCase,
) {

    operator fun invoke(): Flow<List<PreferenceListItem>> {
        return combine(
            getPreference(ColorScheme.Preference),
            getPreference(StartScreen.Preference),
            getAppVersion(),
        ) { colorScheme, startScreen, appVersion ->
            preferences {
                list {
                    title = localization.getString(Res.string.color_scheme)
                    subtitle = localization.getString(colorScheme.labelResource)
                    options = ColorScheme.entries.map { value ->
                        PreferenceListListItem.Option(
                            label = localization.getString(value.labelResource),
                            isSelected = value == colorScheme,
                            onSelected = { setPreference(ColorScheme.Preference, value) },
                        )
                    }
                }
                list {
                    title = localization.getString(Res.string.start_screen)
                    subtitle = localization.getString(startScreen.labelResource)
                    options = StartScreen.entries.map { value ->
                        PreferenceListListItem.Option(
                            label = localization.getString(value.labelResource),
                            isSelected = value == startScreen,
                            onSelected = { setPreference(StartScreen.Preference, value) },
                        )
                    }
                }
                category {
                    title = localization.getString(Res.string.data)
                    icon = Res.drawable.ic_data
                }
                action {
                    title = localization.getString(Res.string.decimal_places)
                    onClick = { openBottomSheet(DecimalPlacesFormScreen) }
                }
                action {
                    title = localization.getString(Res.string.measurement_categories)
                    onClick = { pushScreen(MeasurementCategoryListScreen) }
                }
                action {
                    title = localization.getString(Res.string.tags)
                    onClick = { pushScreen(TagListScreen) }
                }
                action {
                    title = localization.getString(Res.string.food)
                    onClick = { pushScreen(FoodPreferenceScreen) }
                }
                category {
                    title = localization.getString(Res.string.contact)
                    icon = Res.drawable.ic_contact
                }
                action {
                    title = localization.getString(Res.string.homepage)
                    subtitle = localization.getString(Res.string.homepage_url_short)
                    onClick = { urlOpener.open(localization.getString(Res.string.homepage_url)) }
                }
                action {
                    title = localization.getString(Res.string.mail)
                    subtitle = localization.getString(Res.string.mail_url_short)
                    onClick = { urlOpener.open(localization.getString(Res.string.mail_url)) }
                }
                action {
                    title = localization.getString(Res.string.facebook)
                    subtitle = localization.getString(Res.string.facebook_url_short)
                    onClick = { urlOpener.open(localization.getString(Res.string.facebook_url)) }
                }
                category {
                    title = localization.getString(Res.string.about)
                    icon = Res.drawable.ic_about
                }
                action {
                    title = localization.getString(Res.string.source_code)
                    subtitle = localization.getString(Res.string.source_code_url_short)
                    onClick = { urlOpener.open(localization.getString(Res.string.source_code_url)) }
                }
                action {
                    title = localization.getString(Res.string.licenses)
                    onClick = { pushScreen(LicenseListScreen) }
                }
                action {
                    title = localization.getString(Res.string.privacy_policy)
                    onClick = { urlOpener.open(localization.getString(Res.string.privacy_policy_url)) }
                }
                action {
                    title = localization.getString(Res.string.terms_and_conditions)
                    onClick = { urlOpener.open(localization.getString(Res.string.terms_and_conditions_url)) }
                }
                action {
                    title = localization.getString(Res.string.version)
                    subtitle = appVersion
                    onClick = {}
                }
            }
        }
    }
}