package com.faltenreich.diaguard.navigation.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface Modal {

    @Composable
    fun Content(modifier: Modifier = Modifier)
}