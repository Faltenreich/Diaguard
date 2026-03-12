package com.faltenreich.diaguard.preference.overview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.data.navigation.Screen
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.preferences
import com.faltenreich.diaguard.view.bar.TopAppBarStyle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object OverviewPreferenceListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.preferences))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<OverviewPreferenceListViewModel>()
        OverviewPreferenceList(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}