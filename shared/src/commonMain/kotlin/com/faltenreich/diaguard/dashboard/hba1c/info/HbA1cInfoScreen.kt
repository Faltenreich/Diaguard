package com.faltenreich.diaguard.dashboard.hba1c.info

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.bar.top.TopAppBarStyle
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.di.viewModel
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.hba1c
import kotlinx.serialization.Serializable

@Serializable
data object HbA1cInfoScreen : Screen {

    @Composable
    override fun TopAppBar(): TopAppBarStyle {
        return TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.hba1c))
        }
    }

    @Composable
    override fun Content() {
        HbA1cInfo(viewModel = viewModel())
    }
}