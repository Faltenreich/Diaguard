package com.faltenreich.diaguard.preference.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preferences

// FIXME: Supports no state restoration until parameter implements Serializable
data class PreferenceListScreen(val preferences: List<PreferenceListItem>? = null) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.preferences))
        }

    @Composable
    override fun Content() {
        PreferenceList(viewModel = getViewModel { PreferenceListViewModel(preferences) })
    }
}