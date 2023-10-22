package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faltenreich.diaguard.AppTheme

data class DropdownTextMenuItem(
    val label: String,
    val onClick: () -> Unit,
    val isSelected: () -> Boolean,
)

@Composable
fun DropdownTextMenuItem(
    text: String,
    onClick: () -> Unit,
    isSelected: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .background(
                if (isSelected()) AppTheme.colors.scheme.secondaryContainer
                else Color.Transparent
            )
            .padding(all = AppTheme.dimensions.padding.P_3),
    )
}