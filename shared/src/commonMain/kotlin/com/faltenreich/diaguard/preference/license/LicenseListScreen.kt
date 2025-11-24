package com.faltenreich.diaguard.preference.license

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.licenses
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object LicenseListScreen : com.faltenreich.diaguard.navigation.screen.Screen {

    @Composable
    override fun TopAppBar(): com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle {
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.licenses))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<LicenseListViewModel>()
        LicenseList(
            onIntent = viewModel::dispatchIntent,
        )
    }
}