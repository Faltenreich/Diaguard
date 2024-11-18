package com.faltenreich.diaguard.preference.color

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme

@Composable
fun ColorSchemeForm(
    modifier: Modifier = Modifier,
    viewModel: ColorSchemeFormViewModel,
) {
    val state = viewModel.collectState() ?: return

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.color_scheme),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))

        Column(modifier = Modifier.selectableGroup()) {
            ColorSchemeListItem(
                colorScheme = ColorScheme.SYSTEM,
                selection = state.selection,
                onClick = { viewModel.dispatchIntent(ColorSchemeFormIntent.Select(it)) },
            )

            AppTheme(isDarkColorScheme = false) {
                Surface(modifier = Modifier.fillMaxWidth()) {
                    ColorSchemeListItem(
                        colorScheme = ColorScheme.LIGHT,
                        selection = state.selection,
                        onClick = { viewModel.dispatchIntent(ColorSchemeFormIntent.Select(it)) },
                    )
                }
            }

            AppTheme(isDarkColorScheme = true) {
                Surface(modifier = Modifier.fillMaxWidth()) {
                    ColorSchemeListItem(
                        colorScheme = ColorScheme.DARK,
                        selection = state.selection,
                        onClick = { viewModel.dispatchIntent(ColorSchemeFormIntent.Select(it)) },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))
    }
}