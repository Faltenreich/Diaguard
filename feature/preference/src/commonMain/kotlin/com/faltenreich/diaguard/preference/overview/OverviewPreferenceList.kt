package com.faltenreich.diaguard.preference.overview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preference.color.ColorScheme
import com.faltenreich.diaguard.data.preference.startscreen.StartScreen
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.preference.color.ColorSchemeForm
import com.faltenreich.diaguard.preference.decimalplaces.DecimalPlacesForm
import com.faltenreich.diaguard.preference.list.PreferenceActionListItem
import com.faltenreich.diaguard.preference.list.PreferenceCategoryListItem
import com.faltenreich.diaguard.preference.startscreen.StartScreenForm
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.about
import diaguard.shared.generated.resources.backup
import diaguard.shared.generated.resources.backup_read
import diaguard.shared.generated.resources.backup_read_description
import diaguard.shared.generated.resources.backup_write
import diaguard.shared.generated.resources.backup_write_description
import diaguard.shared.generated.resources.color_scheme
import diaguard.shared.generated.resources.color_scheme_dark
import diaguard.shared.generated.resources.color_scheme_light
import diaguard.shared.generated.resources.color_scheme_system
import diaguard.shared.generated.resources.contact
import diaguard.shared.generated.resources.dashboard
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
import diaguard.shared.generated.resources.ic_user
import diaguard.shared.generated.resources.licenses
import diaguard.shared.generated.resources.log
import diaguard.shared.generated.resources.mail
import diaguard.shared.generated.resources.mail_url
import diaguard.shared.generated.resources.mail_url_short
import diaguard.shared.generated.resources.measurement_categories
import diaguard.shared.generated.resources.measurement_units
import diaguard.shared.generated.resources.privacy_policy
import diaguard.shared.generated.resources.privacy_policy_url
import diaguard.shared.generated.resources.reminders
import diaguard.shared.generated.resources.source_code
import diaguard.shared.generated.resources.source_code_url
import diaguard.shared.generated.resources.source_code_url_short
import diaguard.shared.generated.resources.start_screen
import diaguard.shared.generated.resources.tags
import diaguard.shared.generated.resources.terms_and_conditions
import diaguard.shared.generated.resources.terms_and_conditions_url
import diaguard.shared.generated.resources.therapy
import diaguard.shared.generated.resources.timeline
import diaguard.shared.generated.resources.version
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun OverviewPreferenceList(
    state: OverviewPreferenceListState?,
    onIntent: (OverviewPreferenceListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    state ?: return

    var showColorSchemeForm by remember { mutableStateOf(false) }
    var showStartScreenForm by remember { mutableStateOf(false) }
    var showDecimalPlacesForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.color_scheme),
                subtitle = stringResource(
                    when (state.colorScheme) {
                        ColorScheme.SYSTEM -> Res.string.color_scheme_system
                        ColorScheme.LIGHT -> Res.string.color_scheme_light
                        ColorScheme.DARK -> Res.string.color_scheme_dark
                    }
                ),
                onClick = { showColorSchemeForm = true },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.start_screen),
                subtitle = stringResource(
                    when (state.startScreen) {
                        StartScreen.DASHBOARD -> Res.string.dashboard
                        StartScreen.TIMELINE -> Res.string.timeline
                        StartScreen.LOG -> Res.string.log
                    },
                ),
                onClick = { showStartScreenForm = true },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.decimal_places),
                subtitle = state.decimalPlaces.selection.toString(),
                onClick = { showDecimalPlacesForm = true },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.reminders),
                onClick = { onIntent(OverviewPreferenceListIntent.OpenNotificationSettings) },
            )
        }

        stickyHeader {
            PreferenceCategoryListItem(
                title = stringResource(Res.string.therapy),
                icon = Res.drawable.ic_user,
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.measurement_categories),
                onClick = {
                    onIntent(OverviewPreferenceListIntent.NavigateTo(NavigationTarget.MeasurementCategoryList))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.measurement_units),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.NavigateTo(
                            NavigationTarget.MeasurementUnitList(
                                mode = NavigationTarget.MeasurementUnitList.Mode.STROLL,
                            ),
                        ),
                    )
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.tags),
                onClick = {
                    onIntent(OverviewPreferenceListIntent.NavigateTo(NavigationTarget.TagList))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.food),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.NavigateTo(NavigationTarget.FoodPreferenceList)
                    )
                },
            )
        }

        stickyHeader {
            PreferenceCategoryListItem(
                title = stringResource(Res.string.backup),
                icon = Res.drawable.ic_data,
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.backup_write),
                subtitle = stringResource(Res.string.backup_write_description),
                onClick = {
                    onIntent(OverviewPreferenceListIntent.NavigateTo(NavigationTarget.WriteBackupForm))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.backup_read),
                subtitle = stringResource(Res.string.backup_read_description),
                onClick = {
                    onIntent(OverviewPreferenceListIntent.NavigateTo(NavigationTarget.ReadBackupForm))
                },
            )
        }

        stickyHeader {
            PreferenceCategoryListItem(
                title = stringResource(Res.string.contact),
                icon = Res.drawable.ic_contact,
            )
        }
        item {
            val url = stringResource(Res.string.homepage_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.homepage),
                subtitle = stringResource(Res.string.homepage_url_short),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.OpenUrl(
                            url
                        )
                    )
                },
            )
        }
        item {
            val url = stringResource(Res.string.mail_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.mail),
                subtitle = stringResource(Res.string.mail_url_short),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.OpenUrl(
                            url
                        )
                    )
                },
            )
        }
        item {
            val url = stringResource(Res.string.facebook_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.facebook),
                subtitle = stringResource(Res.string.facebook_url_short),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.OpenUrl(
                            url
                        )
                    )
                },
            )
        }

        stickyHeader {
            PreferenceCategoryListItem(
                title = stringResource(Res.string.about),
                icon = Res.drawable.ic_about,
            )
        }
        item {
            val url = stringResource(Res.string.source_code_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.source_code),
                subtitle = stringResource(Res.string.source_code_url_short),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.OpenUrl(
                            url
                        )
                    )
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.licenses),
                onClick = {
                    onIntent(OverviewPreferenceListIntent.NavigateTo(NavigationTarget.LicenseList))
                },
            )
        }
        item {
            val url = stringResource(Res.string.privacy_policy_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.privacy_policy),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.OpenUrl(
                            url
                        )
                    )
                },
            )
        }
        item {
            val url = stringResource(Res.string.terms_and_conditions_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.terms_and_conditions),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.OpenUrl(
                            url
                        )
                    )
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.version),
                subtitle = state.appVersion,
                onClick = {},
            )
        }
    }

    if (showColorSchemeForm) {
        ModalBottomSheet(onDismissRequest = { showColorSchemeForm = false }) {
            ColorSchemeForm(
                state = state.colorScheme,
                onChange = {
                    onIntent(
                        OverviewPreferenceListIntent.SetColorScheme(
                            it
                        )
                    )
                },
            )
        }
    }

    if (showStartScreenForm) {
        ModalBottomSheet(onDismissRequest = { showStartScreenForm = false }) {
            StartScreenForm(
                state = state.startScreen,
                onChange = {
                    onIntent(
                        OverviewPreferenceListIntent.SetStartScreen(
                            it
                        )
                    )
                },
            )
        }
    }

    if (showDecimalPlacesForm) {
        ModalBottomSheet(onDismissRequest = { showDecimalPlacesForm = false }) {
            DecimalPlacesForm(
                state = state.decimalPlaces,
                onChange = {
                    onIntent(
                        OverviewPreferenceListIntent.SetDecimalPlaces(
                            it
                        )
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    OverviewPreferenceList(
        state = OverviewPreferenceListState(
            appVersion = "AppVersion",
            colorScheme = ColorScheme.SYSTEM,
            decimalPlaces = OverviewPreferenceListState.DecimalPlaces(
                selection = 1,
                illustration = "Illustration",
                enableDecreaseButton = true,
                enableIncreaseButton = true,
            ),
            startScreen = StartScreen.DASHBOARD,
        ),
        onIntent = {},
    )
}