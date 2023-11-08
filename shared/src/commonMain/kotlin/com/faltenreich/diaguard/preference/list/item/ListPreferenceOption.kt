package com.faltenreich.diaguard.preference.list.item

import androidx.compose.runtime.Composable

data class ListPreferenceOption(
    val label: @Composable () -> String,
    val isSelected: Boolean,
    val onSelected: suspend () -> Unit,
)