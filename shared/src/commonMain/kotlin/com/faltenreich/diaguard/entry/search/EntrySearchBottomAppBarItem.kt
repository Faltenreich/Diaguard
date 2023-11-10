package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun EntrySearchBottomAppBarItem() {
    val navigator = LocalNavigator.currentOrThrow
    BottomAppBarItem(
        painter = painterResource(MR.images.ic_search),
        contentDescription = MR.strings.search_open,
        onClick = { navigator.push(EntrySearchScreen()) },
    )
}