package com.faltenreich.diaguard.preference.license

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.data.navigation.TopAppBarStyle
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.licenses
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