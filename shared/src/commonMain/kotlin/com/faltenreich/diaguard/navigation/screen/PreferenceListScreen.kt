package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.preference.list.PreferenceList
import com.faltenreich.diaguard.preference.list.PreferenceListViewModel
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import org.koin.core.parameter.parametersOf

// FIXME: Supports no state restoration until parameter implements Serializable
data class PreferenceListScreen(val preferences: List<PreferenceListItem>? = null) : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(MR.strings.preferences))
        }

    @Composable
    override fun Content() {
        val viewModel = getViewModel<PreferenceListViewModel> { parametersOf(preferences) }
        PreferenceList(viewModel = viewModel)
    }
}