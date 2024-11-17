package com.faltenreich.diaguard.preference.decimal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.decimal_places
import diaguard.shared.generated.resources.decimal_places_description
import diaguard.shared.generated.resources.decimal_places_update
import diaguard.shared.generated.resources.ic_remove
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DecimalPlacesForm(
    modifier: Modifier = Modifier,
    viewModel: DecimalPlacesFormViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimensions.padding.P_3_5),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = getString(Res.string.decimal_places),
            style = AppTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_2))
        Text(getString(Res.string.decimal_places_description))

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3_5))

        Row(
            modifier = Modifier.background(
                color = AppTheme.colors.scheme.primaryContainer,
                shape = AppTheme.shapes.medium,
            ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    val decimalPlaces = state.decimalPlaces - 1
                    viewModel.dispatchIntent(DecimalPlacesFormIntent.Update(decimalPlaces))
                },
                enabled = state.enableDecreaseButton,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_remove),
                    contentDescription = stringResource(
                        Res.string.decimal_places_update,
                        state.decimalPlaces - 1,
                    ),
                )
            }

            Text(
                text = state.decimalPlaces.toString(),
                color = AppTheme.colors.scheme.onPrimary,
            )

            IconButton(
                onClick = {
                    val decimalPlaces = state.decimalPlaces + 1
                    viewModel.dispatchIntent(DecimalPlacesFormIntent.Update(decimalPlaces))
                },
                enabled = state.enableIncreaseButton,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(
                        Res.string.decimal_places_update,
                        state.decimalPlaces + 1,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Text(state.illustration)

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_4_5))
    }
}