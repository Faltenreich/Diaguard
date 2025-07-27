package com.faltenreich.diaguard.preference.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.shared.view.preview.AppPreview
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview
@Composable
private fun Preview() = AppPreview {
    LicenseList(
        state = LicenseListState(
            libraries = Libs(
                libraries = persistentListOf(
                    Library(
                        uniqueId = "uniqueId",
                        artifactVersion = "1.0.0",
                        name = "Library",
                        description = "Description",
                        website = null,
                        developers = persistentListOf(
                            Developer(
                                name = "Developer",
                                organisationUrl = null,
                            ),
                        ),
                        organization = null,
                        scm = null,
                        licenses = persistentSetOf(
                            License(
                                name = "License",
                                url = null,
                                hash = "hash",
                            )
                        ),
                    )
                ),
                licenses = persistentSetOf(),
            ),
        ),
        onIntent = {},
    )
}