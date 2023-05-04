package com.faltenreich.diaguard.timeline

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle

class Timeline : Screen {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Text("Timeline")
    }
}