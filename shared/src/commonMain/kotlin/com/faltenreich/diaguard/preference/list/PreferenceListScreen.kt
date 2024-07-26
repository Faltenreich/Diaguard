package com.faltenreich.diaguard.preference.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preferences
import kotlinx.serialization.Serializable

@Serializable
data object PreferenceListScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.preferences))
        }

    @Composable
    override fun Content() {
        PreferenceList(viewModel = getViewModel { PreferenceListViewModel() })
    }
}