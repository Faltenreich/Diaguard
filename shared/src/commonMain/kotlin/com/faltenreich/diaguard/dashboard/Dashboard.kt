package com.faltenreich.diaguard.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.entry.list.EntryList

@Composable
fun Dashboard(modifier: Modifier = Modifier) {
    EntryList(modifier = modifier)
}