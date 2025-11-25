package com.faltenreich.diaguard.preference.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import diaguard.feature.preference.generated.resources.Res
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun LicenseList(
    onIntent: (LicenseListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val libraries by produceLibraries { Res.readBytes("files/aboutlibraries.json").decodeToString() }
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
        onLibraryClick = { library -> onIntent(LicenseListIntent.OpenWebsite(library)) },
    )
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    LicenseList(onIntent = {})
}