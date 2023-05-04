package com.faltenreich.diaguard.entry.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.EntrySearchTarget
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem

@Composable
fun EntrySearchBottomAppBarItem() {
    val navigator = LocalNavigator.currentOrThrow
    BottomAppBarItem(
        image = Icons.Filled.Search,
        contentDescription = MR.strings.search_open,
        onClick = { navigator.push(EntrySearchTarget()) },
    )
}