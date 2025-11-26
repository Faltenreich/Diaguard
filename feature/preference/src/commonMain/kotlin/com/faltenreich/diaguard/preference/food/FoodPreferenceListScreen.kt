package com.faltenreich.diaguard.preference.food

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.injection.viewModel
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.food
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data object FoodPreferenceListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
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