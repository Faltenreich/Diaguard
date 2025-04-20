package com.faltenreich.diaguard.preference.overview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.color.ColorSchemeForm
import com.faltenreich.diaguard.preference.decimal.DecimalPlacesForm
import com.faltenreich.diaguard.preference.list.item.action
import com.faltenreich.diaguard.preference.list.item.category
import com.faltenreich.diaguard.preference.list.preferences
import com.faltenreich.diaguard.shared.di.viewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme
import diaguard.shared.generated.resources.decimal_places
import diaguard.shared.generated.resources.ic_user
import diaguard.shared.generated.resources.therapy
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverviewPreferenceList(
    viewModel: OverviewPreferenceListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState() ?: return

    var showColorSchemeForm by remember { mutableStateOf(false) }
    var showDecimalPlacesForm by remember { mutableStateOf(false) }

    preferences(modifier = modifier.fillMaxSize()) {
        action(
            title = { stringResource(Res.string.color_scheme) },
            subtitle = { stringResource(state.colorScheme.labelResource) },
            onClick = { showColorSchemeForm = true },
        )
        action(
            title = { stringResource(Res.string.decimal_places) },
            subtitle = { state.decimalPlaces.toString() },
            onClick = { showDecimalPlacesForm = true },
        )
        category(
            title = { stringResource(Res.string.therapy) },
            icon = Res.drawable.ic_user,
        )
    }

    if (showColorSchemeForm) {
        ModalBottomSheet(onDismissRequest = { showColorSchemeForm = false }) {
            ColorSchemeForm(viewModel = viewModel())
        }
    }

    if (showDecimalPlacesForm) {
        ModalBottomSheet(onDismissRequest = { showDecimalPlacesForm = false }) {
            DecimalPlacesForm(viewModel = viewModel())
        }
    }
}