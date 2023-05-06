package com.faltenreich.diaguard.shared.view

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    androidx.compose.material3.FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
        content = content,
    )
}