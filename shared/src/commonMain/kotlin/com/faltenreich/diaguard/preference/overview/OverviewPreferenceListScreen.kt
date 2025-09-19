package com.faltenreich.diaguard.preference.overview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preferences
import kotlinx.serialization.Serializable

@Serializable
data object OverviewPreferenceListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.preferences))
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