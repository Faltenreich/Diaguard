package com.faltenreich.diaguard.entry.form

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.Entry
import com.faltenreich.diaguard.shared.view.FloatingActionButton
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EntryFormFloatingActionButton(
    entry: Entry? = null,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    FloatingActionButton(
        onClick = { navigator.push(EntryForm(entry)) },
        modifier = modifier,
    ) {
        Icon(Icons.Filled.Add, stringResource(MR.strings.entry_new_description))
    }
}