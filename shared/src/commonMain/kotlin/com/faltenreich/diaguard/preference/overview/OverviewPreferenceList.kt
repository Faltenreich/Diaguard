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
import com.faltenreich.diaguard.backup.user.read.ReadBackupFormScreen
import com.faltenreich.diaguard.backup.user.write.WriteBackupFormScreen
import com.faltenreich.diaguard.measurement.category.list.MeasurementCategoryListScreen
import com.faltenreich.diaguard.measurement.unit.list.MeasurementUnitListScreen
import com.faltenreich.diaguard.preference.color.ColorSchemeForm
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesForm
import com.faltenreich.diaguard.preference.food.FoodPreferenceListScreen
import com.faltenreich.diaguard.preference.license.LicenseListScreen
import com.faltenreich.diaguard.preference.list.item.PreferenceActionListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceCategoryListItem2
import com.faltenreich.diaguard.preference.screen.StartScreenForm
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.tag.list.TagListScreen
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.about
import diaguard.shared.generated.resources.backup
import diaguard.shared.generated.resources.backup_read
import diaguard.shared.generated.resources.backup_read_description
import diaguard.shared.generated.resources.backup_write
import diaguard.shared.generated.resources.backup_write_description
import diaguard.shared.generated.resources.color_scheme
import diaguard.shared.generated.resources.contact
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
import diaguard.shared.generated.resources.version
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverviewPreferenceList(
    viewModel: OverviewPreferenceListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

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
                subtitle = state.decimalPlaces.toString(),
                onClick = { showDecimalPlacesForm = true },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.reminders),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.OpenNotificationSettings) },
            )
        }
        stickyHeader {
            PreferenceCategoryListItem2(
                title = stringResource(Res.string.therapy),
                icon = Res.drawable.ic_user,
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.measurement_categories),
                onClick = {
                    viewModel.dispatchIntent(OverviewPreferenceListIntent.PushScreen(MeasurementCategoryListScreen))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.measurement_units),
                onClick = {
                    viewModel.dispatchIntent(OverviewPreferenceListIntent.PushScreen(MeasurementUnitListScreen))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.tags),
                onClick = {
                    viewModel.dispatchIntent(OverviewPreferenceListIntent.PushScreen(TagListScreen))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.food),
                onClick = {
                    viewModel.dispatchIntent(OverviewPreferenceListIntent.PushScreen(FoodPreferenceListScreen))
                },
            )
        }
        stickyHeader {
            PreferenceCategoryListItem2(
                title = stringResource(Res.string.backup),
                icon = Res.drawable.ic_data,
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.backup_write),
                subtitle = stringResource(Res.string.backup_write_description),
                onClick = {
                    viewModel.dispatchIntent(OverviewPreferenceListIntent.PushScreen(WriteBackupFormScreen))
                },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.backup_read),
                subtitle = stringResource(Res.string.backup_read_description),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.PushScreen(ReadBackupFormScreen))
                },
            )
        }
        stickyHeader {
            PreferenceCategoryListItem2(
                title = stringResource(Res.string.contact),
                icon = Res.drawable.ic_contact,
            )
        }
        item {
            val url = stringResource(Res.string.homepage_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.homepage),
                subtitle = stringResource(Res.string.homepage_url_short),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.OpenUrl(url)) },
            )
        }
        item {
            val url = stringResource(Res.string.mail_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.mail),
                subtitle = stringResource(Res.string.mail_url_short),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.OpenUrl(url)) },
            )
        }
        item {
            val url = stringResource(Res.string.facebook_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.facebook),
                subtitle = stringResource(Res.string.facebook_url_short),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.OpenUrl(url)) },
            )
        }
        stickyHeader {
            PreferenceCategoryListItem2(
                title = stringResource(Res.string.about),
                icon = Res.drawable.ic_about,
            )
        }
        item {
            val url = stringResource(Res.string.source_code_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.source_code),
                subtitle = stringResource(Res.string.source_code_url_short),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.OpenUrl(url)) },
            )
        }
        item {
            PreferenceActionListItem(
                title = stringResource(Res.string.licenses),
                onClick = {
                    viewModel.dispatchIntent(OverviewPreferenceListIntent.PushScreen(LicenseListScreen))
                },
            )
        }
        item {
            val url = stringResource(Res.string.privacy_policy_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.privacy_policy),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.OpenUrl(url)) },
            )
        }
        item {
            val url = stringResource(Res.string.terms_and_conditions_url)
            PreferenceActionListItem(
                title = stringResource(Res.string.terms_and_conditions),
                onClick = { viewModel.dispatchIntent(OverviewPreferenceListIntent.OpenUrl(url)) },
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

    // TODO: Migrate ViewModels into OverviewPreferenceListViewModel

    if (showColorSchemeForm) {
        ModalBottomSheet(onDismissRequest = { showColorSchemeForm = false }) {
            ColorSchemeForm(viewModel = viewModel())
        }
    }

    if (showStartScreenForm) {
        ModalBottomSheet(onDismissRequest = { showStartScreenForm = false }) {
            StartScreenForm(viewModel = viewModel())
        }
    }

    if (showDecimalPlacesForm) {
        ModalBottomSheet(onDismissRequest = { showDecimalPlacesForm = false }) {
            DecimalPlacesForm(viewModel = viewModel())
        }
    }
}