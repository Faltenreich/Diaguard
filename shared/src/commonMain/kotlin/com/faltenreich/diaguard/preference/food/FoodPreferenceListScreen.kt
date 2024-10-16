package com.faltenreich.diaguard.preference.food

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.food
import kotlinx.serialization.Serializable

@Serializable
data object FoodPreferenceListScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.food))
        }
    }

    @Composable
    override fun Content() {
        TODO()
    }
}