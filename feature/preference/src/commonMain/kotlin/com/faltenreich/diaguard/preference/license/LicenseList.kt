package com.faltenreich.diaguard.preference.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.faltenreich.diaguard.data.preview.PreviewScaffold
import com.faltenreich.diaguard.resource.Res
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries

@Composable
internal fun LicenseList(
    onIntent: (LicenseListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // ./gradlew exportLibraryDefinitions -PexportPath=src/commonMain/composeResources/files/
    // FIXME: Missing resource with path: composeResources/com.faltenreich.diaguard.resource/files/aboutlibraries.json
    val libraries by produceLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    LibrariesContainer(
        libraries = libraries,
        modifier = modifier.fillMaxSize(),
        onLibraryClick = { library -> onIntent(LicenseListIntent.OpenWebsite(library)); true },
    )
}

@Preview
@Composable
private fun Preview() = PreviewScaffold {
    LicenseList(onIntent = {})
}