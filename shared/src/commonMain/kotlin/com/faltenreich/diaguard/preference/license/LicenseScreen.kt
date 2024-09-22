package com.faltenreich.diaguard.preference.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.navigation.screen.Screen
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
import diaguard.shared.generated.resources.Res
import kotlinx.serialization.Serializable

@Serializable
data object LicenseScreen : Screen {

    @Composable
    override fun Content() {
        val libraries by rememberLibraries {
            // TODO: Generate file
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }
        LibrariesContainer(
            libraries = libraries,
            modifier = Modifier.fillMaxSize(),
        )
    }
}