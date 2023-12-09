package com.faltenreich.diaguard.entry.form

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.navigation.Navigation
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun EntryFormFloatingActionButton(
    entry: Entry? = null,
    modifier: Modifier = Modifier,
    navigation: Navigation = inject(),
) {
    FloatingActionButton(
        onClick = { navigation.push(EntryFormScreen(entry)) },
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(MR.images.ic_add),
            contentDescription = getString(MR.strings.entry_new_description),
        )
    }
}