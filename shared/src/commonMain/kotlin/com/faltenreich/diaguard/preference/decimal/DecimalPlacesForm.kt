package com.faltenreich.diaguard.preference.decimal

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.decimal_places
import diaguard.shared.generated.resources.decimal_places_example

@Composable
fun DecimalPlacesForm(
    modifier: Modifier = Modifier,
    viewModel: DecimalPlacesFormViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return
    Column(modifier = modifier) {
        Text(
            text = getString(Res.string.decimal_places),
            style = AppTheme.typography.labelMedium,
        )
        Text(
            text = getString(
                Res.string.decimal_places_example,
                state.decimalPlaces.toString(),
                state.decimalPlaces.toString(),
            ),
            style = AppTheme.typography.bodyMedium,
        )
        Slider(
            value = state.decimalPlaces.toFloat(),
            onValueChange = { value ->
                viewModel.dispatchIntent(DecimalPlacesFormIntent.SetDecimalPlaces(value.toInt()))
            },
            steps = 2,
            valueRange = 0f .. 3f
        )
    }
}