package com.faltenreich.diaguard.preference.food

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object FoodPreferenceListScreen : com.faltenreich.diaguard.navigation.screen.Screen {

    @Composable
    override fun TopAppBar(): com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle {
        return _root_ide_package_.com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle.CenterAligned {
            Text(stringResource(Res.string.food))
        }
    }

    @Composable
    override fun Content() {
        val viewModel = viewModel<FoodPreferenceListViewModel>()
        FoodPreferenceList(
            state = viewModel.collectState(),
            onIntent = viewModel::dispatchIntent,
        )
    }
}