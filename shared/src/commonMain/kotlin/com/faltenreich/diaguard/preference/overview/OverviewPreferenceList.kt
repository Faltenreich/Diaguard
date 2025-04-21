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
import com.faltenreich.diaguard.preference.color.ColorSchemeForm
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesForm
import com.faltenreich.diaguard.preference.list.item.PreferenceActionListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceCategoryListItem2
import com.faltenreich.diaguard.preference.screen.StartScreenForm
import com.faltenreich.diaguard.shared.di.viewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme
import diaguard.shared.generated.resources.decimal_places
import diaguard.shared.generated.resources.ic_user
import diaguard.shared.generated.resources.reminders
import diaguard.shared.generated.resources.start_screen
import diaguard.shared.generated.resources.therapy
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
        item {
            PreferenceCategoryListItem2(
                title = stringResource(Res.string.therapy),
                icon = Res.drawable.ic_user,
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