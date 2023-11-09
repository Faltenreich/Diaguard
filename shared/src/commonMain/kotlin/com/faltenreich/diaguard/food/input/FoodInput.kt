package com.faltenreich.diaguard.food.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun FoodInput(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
) {
    TextButton(
        onClick = onAdd,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(getString(MR.strings.food_add))
    }
}