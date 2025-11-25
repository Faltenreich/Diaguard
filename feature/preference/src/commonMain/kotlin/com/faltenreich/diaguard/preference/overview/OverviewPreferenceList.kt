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
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.preference.color.ColorScheme
import com.faltenreich.diaguard.preference.color.ColorSchemeForm
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesForm
import com.faltenreich.diaguard.preference.food.FoodPreferenceListScreen
import com.faltenreich.diaguard.preference.list.item.PreferenceActionListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceCategoryListItem
import com.faltenreich.diaguard.preference.screen.StartScreen
import com.faltenreich.diaguard.preference.screen.StartScreenForm
import diaguard.feature.preference.generated.resources.Res
import diaguard.feature.preference.generated.resources.about
import diaguard.feature.preference.generated.resources.backup
import diaguard.feature.preference.generated.resources.backup_read
import diaguard.feature.preference.generated.resources.backup_read_description
import diaguard.feature.preference.generated.resources.backup_write
import diaguard.feature.preference.generated.resources.backup_write_description
import diaguard.feature.preference.generated.resources.color_scheme
import diaguard.feature.preference.generated.resources.contact
import diaguard.feature.preference.generated.resources.decimal_places
import diaguard.feature.preference.generated.resources.facebook
import diaguard.feature.preference.generated.resources.facebook_url
import diaguard.feature.preference.generated.resources.facebook_url_short
import diaguard.feature.preference.generated.resources.food
import diaguard.feature.preference.generated.resources.homepage
import diaguard.feature.preference.generated.resources.homepage_url
import diaguard.feature.preference.generated.resources.homepage_url_short
import diaguard.feature.preference.generated.resources.ic_about
import diaguard.feature.preference.generated.resources.ic_contact
import diaguard.feature.preference.generated.resources.ic_data
import diaguard.feature.preference.generated.resources.ic_user
import diaguard.feature.preference.generated.resources.licenses
import diaguard.feature.preference.generated.resources.mail
import diaguard.feature.preference.generated.resources.mail_url
import diaguard.feature.preference.generated.resources.mail_url_short
import diaguard.feature.preference.generated.resources.measurement_categories
import diaguard.feature.preference.generated.resources.measurement_units
import diaguard.feature.preference.generated.resources.privacy_policy
import diaguard.feature.preference.generated.resources.privacy_policy_url
import diaguard.feature.preference.generated.resources.reminders
import diaguard.feature.preference.generated.resources.source_code
import diaguard.feature.preference.generated.resources.source_code_url
import diaguard.feature.preference.generated.resources.source_code_url_short
import diaguard.feature.preference.generated.resources.start_screen
import diaguard.feature.preference.generated.resources.tags
import diaguard.feature.preference.generated.resources.terms_and_conditions
import diaguard.feature.preference.generated.resources.terms_and_conditions_url
import diaguard.feature.preference.generated.resources.therapy
import diaguard.feature.preference.generated.resources.version
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OverviewPreferenceList(
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
                subtitle = stringResource(state.colorScheme.labelResource),
                onClick = { showColorSchemeForm = true },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.start_screen),
                subtitle = stringResource(state.startScreen.labelResource),
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
                    // TODO: onIntent(OverviewPreferenceListIntent.PushScreen(MeasurementCategoryListScreen))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.measurement_units),
                onClick = {
                    // TODO: onIntent(OverviewPreferenceListIntent.PushScreen(MeasurementUnitListScreen(mode = MeasurementUnitListMode.STROLL)))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.tags),
                onClick = {
                    // TODO: onIntent(OverviewPreferenceListIntent.PushScreen(TagListScreen))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.food),
                onClick = {
                    onIntent(
                        OverviewPreferenceListIntent.PushScreen(
                            FoodPreferenceListScreen
                        )
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
                    // TODO: onIntent(OverviewPreferenceListIntent.PushScreen(WriteBackupFormScreen))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.backup_read),
                subtitle = stringResource(Res.string.backup_read_description),
                onClick = {
                    // TODO: onIntent(OverviewPreferenceListIntent.PushScreen(ReadBackupFormScreen))
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