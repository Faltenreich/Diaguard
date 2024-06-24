package com.faltenreich.diaguard.preference.decimal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.decimal_places

@Composable
fun DecimalPlacesForm(
    modifier: Modifier = Modifier,
    viewModel: DecimalPlacesFormViewModel = inject(),
) {
    val state = viewModel.collectState() ?: return
    Column(modifier = modifier.padding(horizontal = AppTheme.dimensions.padding.P_3)) {
        Text(
            text = getString(Res.string.decimal_places),
            style = AppTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_3))

        Text(state.illustration)

        // TODO: Indicate current value
        // FIXME: Locked after hiding BottomSheet
        Slider(
            value = state.decimalPlaces.toFloat(),
            onValueChange = { value ->
                viewModel.dispatchIntent(DecimalPlacesFormIntent.SetDecimalPlaces(value.toInt()))
            },
            steps = state.range.last - 1,
            valueRange = state.range.first.toFloat() .. state.range.last.toFloat(),
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.padding.P_4))
    }
}