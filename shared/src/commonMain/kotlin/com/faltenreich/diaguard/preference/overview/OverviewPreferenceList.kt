package com.faltenreich.diaguard.preference.overview

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.preference.color.ColorSchemeForm
import com.faltenreich.diaguard.preference.color.ColorSchemeFormScreen
import com.faltenreich.diaguard.preference.list.PreferenceList
import com.faltenreich.diaguard.preference.list.action
import com.faltenreich.diaguard.preference.list.preferences
import com.faltenreich.diaguard.shared.di.viewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverviewPreferenceList(
    viewModel: OverviewPreferenceListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.collectState()
    PreferenceList(
        items = state?.items ?: emptyList(),
        modifier = modifier,
    )

    var showColorSchemeForm by remember { mutableStateOf(false) }

    preferences {
        action(
            title = {
                Text(stringResource(Res.string.color_scheme))
                Text(
                    text =
                )
            },
            title = { stringResource(Res.string.color_scheme) },
            subtitle = stringResource(colorScheme.labelResource),
            onClick = { showColorSchemeForm = true },
        )
    }

    if (showColorSchemeForm) {
        ColorSchemeForm(viewModel = viewModel())
    }
}