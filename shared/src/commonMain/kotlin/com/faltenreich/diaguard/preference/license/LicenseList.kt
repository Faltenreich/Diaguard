package com.faltenreich.diaguard.preference.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.rememberLibraries
import diaguard.shared.generated.resources.Res
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LicenseList(
    onIntent: (LicenseListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val libraries by rememberLibraries { Res.readBytes("files/aboutlibraries.json").decodeToString() }
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
        onLibraryClick = { library -> onIntent(LicenseListIntent.OpenWebsite(library)) },
    )
}

@Preview
@Composable
private fun Preview() = AppPreview {
    LicenseList(onIntent = {})
}