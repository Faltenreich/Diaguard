package com.faltenreich.diaguard.preference.decimal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var value by rememberSaveable { mutableStateOf(0f) }
    Column(
        modifier = modifier.padding(all = AppTheme.dimensions.padding.P_2),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_1),
    ) {
        Text(
            text = getString(Res.string.decimal_places),
            style = AppTheme.typography.labelMedium,
        )
        Text(
            text = getString(Res.string.decimal_places_example, value.toString()),
            style = AppTheme.typography.bodyMedium,
        )
        Slider(
            value = value,
            onValueChange = { value = it },
            steps = 3,
        )
    }
}