package com.faltenreich.diaguard.entry.form

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.entry.Entry
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EntryFormFloatingActionButton(
    entry: Entry? = null,
) {
    val navigator = LocalNavigator.currentOrThrow
    FloatingActionButton(
        onClick = { navigator.push(EntryForm(entry)) },
        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
    ) {
        Icon(Icons.Filled.Add, stringResource(MR.strings.entry_new_description))
    }
}