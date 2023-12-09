package com.faltenreich.diaguard.entry.search

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarItem
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.di.inject
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun EntrySearchBottomAppBarItem(
    navigation: Navigation = inject(),
) {
    BottomAppBarItem(
        painter = painterResource(MR.images.ic_search),
        contentDescription = MR.strings.search_open,
        onClick = { navigation.push(EntrySearchScreen()) },
    )
}