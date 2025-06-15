package com.faltenreich.diaguard.preference.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun LicenseList(
    viewModel: LicenseListViewModel,
    modifier: Modifier = Modifier,
) {
    when (val state = viewModel.collectState()) {
        null -> Unit
        else -> LibrariesContainer(
            libraries = state.libraries,
            modifier = modifier.fillMaxSize(),
            onLibraryClick = { library -> viewModel.dispatchIntent(LicenseListIntent.OpenWebsite(library)) },
        )
    }
}