package com.faltenreich.diaguard.preference.color

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.preference.ColorScheme
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.color_scheme
import diaguard.shared.generated.resources.color_scheme_dark
import diaguard.shared.generated.resources.color_scheme_light
import diaguard.shared.generated.resources.color_scheme_system

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

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3_5))

        Row(
            modifier = Modifier
                .selectable(
                    selected = state.selection == ColorScheme.SYSTEM,
                    onClick = {
                        viewModel.dispatchIntent(ColorSchemeFormIntent.Select(ColorScheme.SYSTEM))
                    },
                )
                .padding(all = AppTheme.dimensions.padding.P_3_5),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = getString(Res.string.color_scheme_system),
                modifier = Modifier.weight(1f),
            )
            RadioButton(
                selected = state.selection == ColorScheme.SYSTEM,
                onClick = null,
            )
        }

        AppTheme(isDarkColorScheme = false) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.colors.scheme.surface,
            ) {
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = state.selection == ColorScheme.LIGHT,
                            onClick = {
                                viewModel.dispatchIntent(ColorSchemeFormIntent.Select(ColorScheme.LIGHT))
                            },
                        )
                        .padding(all = AppTheme.dimensions.padding.P_3_5),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = getString(Res.string.color_scheme_light),
                        modifier = Modifier.weight(1f),
                    )
                    RadioButton(
                        selected = state.selection == ColorScheme.LIGHT,
                        onClick = null,
                    )
                }
            }
        }
        AppTheme(isDarkColorScheme = true) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.colors.scheme.surface,
            ) {
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = state.selection == ColorScheme.DARK,
                            onClick = {
                                viewModel.dispatchIntent(ColorSchemeFormIntent.Select(ColorScheme.DARK))
                            },
                        )
                        .padding(all = AppTheme.dimensions.padding.P_3_5),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = getString(Res.string.color_scheme_dark),
                        modifier = Modifier.weight(1f),
                    )
                    RadioButton(
                        selected = state.selection == ColorScheme.DARK,
                        onClick = null,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))
    }
}