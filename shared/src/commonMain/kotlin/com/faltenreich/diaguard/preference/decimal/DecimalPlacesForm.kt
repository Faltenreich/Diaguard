package com.faltenreich.diaguard.preference.decimal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.view.theme.AppTheme
import com.faltenreich.diaguard.preference.overview.OverviewPreferenceListState
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import diaguard.core.view.generated.resources.ic_add
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.decimal_places
import diaguard.shared.generated.resources.decimal_places_description
import diaguard.shared.generated.resources.decimal_places_update
import diaguard.shared.generated.resources.ic_remove
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DecimalPlacesForm(
    state: OverviewPreferenceListState.DecimalPlaces,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimensions.padding.P_3_5),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.decimal_places),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Text(
            text = stringResource(Res.string.decimal_places_description),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Row(
            modifier = Modifier.background(
                color = AppTheme.colors.scheme.surfaceContainerHighest,
                shape = AppTheme.shapes.medium,
            ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onChange(state.selection - 1) },
                enabled = state.enableDecreaseButton,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_remove),
                    contentDescription = stringResource(
                        Res.string.decimal_places_update,
                        state.selection - 1,
                    ),
                )
            }

            Text(
                text = state.selection.toString(),
                color = AppTheme.colors.scheme.onSurface,
            )

            IconButton(
                onClick = { onChange(state.selection + 1) },
                enabled = state.enableIncreaseButton,
            ) {
                Icon(
                    imageVector = vectorResource(diaguard.core.view.generated.resources.Res.drawable.ic_add),
                    contentDescription = stringResource(
                        Res.string.decimal_places_update,
                        state.selection + 1,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Text(
            text = state.illustration,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_4))
    }
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    DecimalPlacesForm(
        state = OverviewPreferenceListState.DecimalPlaces(
            selection = 1,
            illustration = "Illustration",
            enableDecreaseButton = true,
            enableIncreaseButton = true,
        ),
        onChange = {},
    )
}