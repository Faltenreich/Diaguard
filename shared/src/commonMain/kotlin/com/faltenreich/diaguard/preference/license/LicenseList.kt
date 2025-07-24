package com.faltenreich.diaguard.preference.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun LicenseList(
    state: LicenseListState?,
    onIntent: (LicenseListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        null -> Unit
        else -> LibrariesContainer(
            libraries = state.libraries,
            modifier = modifier.fillMaxSize(),
            onLibraryClick = { library -> onIntent(LicenseListIntent.OpenWebsite(library)) },
        )
    }
}