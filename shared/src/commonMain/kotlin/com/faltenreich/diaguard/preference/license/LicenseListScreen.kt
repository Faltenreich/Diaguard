package com.faltenreich.diaguard.preference.license

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.localization.di.viewModel
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.licenses
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object LicenseListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
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