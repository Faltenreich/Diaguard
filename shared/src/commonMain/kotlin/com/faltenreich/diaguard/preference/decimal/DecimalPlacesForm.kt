package com.faltenreich.diaguard.preference.decimal

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.BottomSheet

@Composable
fun DecimalPlacesForm(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DecimalPlacesFormViewModel = inject(),
) {
    var value by rememberSaveable { mutableStateOf(0f) }
    BottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        Column(modifier = modifier) {

        }
        Slider(
            value = value,
            onValueChange = { value = it },
            steps = 3,
        )
    }
}