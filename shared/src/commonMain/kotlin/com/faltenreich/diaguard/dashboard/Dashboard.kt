package com.faltenreich.diaguard.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.entry.form.EntryFormFloatingActionButton
import com.faltenreich.diaguard.entry.search.EntrySearchBottomAppBarItem
import com.faltenreich.diaguard.navigation.NavigationTarget
import com.faltenreich.diaguard.navigation.bottom.BottomAppBarStyle

class Dashboard : NavigationTarget {

    override val bottomAppBarStyle = BottomAppBarStyle.Visible(
        actions = { EntrySearchBottomAppBarItem() },
        floatingActionButton = { EntryFormFloatingActionButton() },
    )

    @Composable
    override fun Content() {
        Text("Dashboard")
    }
}