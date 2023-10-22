package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.faltenreich.diaguard.AppTheme

@Composable
fun TextDivider(
    label: String,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RectangleShape,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppTheme.dimensions.padding.P_3,
                    vertical = AppTheme.dimensions.padding.P_3,
                ),
        )
    }
}