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
import diaguard.shared.generated.resources.ic_remove
import org.jetbrains.compose.resources.painterResource

@Composable
fun DecimalPlacesForm(
    modifier: Modifier = Modifier,
    viewModel: DecimalPlacesFormViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimensions.padding.P_3),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3),
    ) {
        Text(
            text = getString(Res.string.decimal_places),
            style = AppTheme.typography.titleLarge,
        )

        Text(state.illustration)

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
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_remove),
                    contentDescription = null,
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
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_4))
    }
}