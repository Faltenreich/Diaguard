package com.faltenreich.diaguard.shared.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect fun Modifier.keyboardPadding(): Modifier

@Composable
expect fun SetIsAppearanceLightStatusBars(isAppearanceLightStatusBars: Boolean)